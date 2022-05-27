package ai.deepsense.deeplang.actionobjects

import org.apache.spark.sql.types.StructType

import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.actionobjects.Projector.ColumnProjection
import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.actionobjects.dataframe.DataFrameColumnsGetter
import ai.deepsense.deeplang.parameters.selections.SingleColumnSelection
import ai.deepsense.deeplang.parameters._
import ai.deepsense.deeplang.parameters.choice.Choice
import ai.deepsense.deeplang.parameters.choice.ChoiceParameter
import ai.deepsense.deeplang.utils.SparkUtils

class Projector extends Transformer {

  val projectionColumns = ParamsSequence[ColumnProjection](
    name = "projection columns",
    description = Some("Column to project in the output DataFrame.")
  )

  def getProjectionColumns: Seq[ColumnProjection] = $(projectionColumns)

  def setProjectionColumns(value: Seq[ColumnProjection]): this.type = set(projectionColumns, value)

  override val params: Array[ai.deepsense.deeplang.parameters.Parameter[_]] = Array(projectionColumns)

  override def applyTransform(ctx: ExecutionContext, df: DataFrame): DataFrame = {
    val exprSeq = getProjectionColumns.map { cp =>
      val renameExpressionPart = cp.getRenameColumn.getColumnName match {
        case None             => ""
        case Some(columnName) => s" AS ${SparkUtils.escapeColumnName(columnName)}"
      }
      SparkUtils.escapeColumnName(df.getColumnName(cp.getOriginalColumn)) + renameExpressionPart
    }
    if (exprSeq.isEmpty)
      DataFrame.empty(ctx)
    else {
      val filtered = df.sparkDataFrame.selectExpr(exprSeq: _*)
      DataFrame.fromSparkDataFrame(filtered)
    }
  }

  override def applyTransformSchema(schema: StructType): Option[StructType] = {
    val namesPairsSeq = getProjectionColumns.map { cp =>
      val originalColumnName = DataFrameColumnsGetter.getColumnName(schema, cp.getOriginalColumn)
      val resultColumnName   = cp.getRenameColumn.getColumnName match {
        case None             => originalColumnName
        case Some(columnName) => columnName
      }
      (originalColumnName, resultColumnName)
    }
    val fields        = namesPairsSeq.map { case (originalColumnName: String, renamedColumnName: String) =>
      schema(originalColumnName).copy(name = renamedColumnName)
    }
    Some(StructType(fields))
  }

}

object Projector {

  val OriginalColumnParameterName = "original column"

  val RenameColumnParameterName = "rename column"

  val ColumnNameParameterName = "column name"

  case class ColumnProjection() extends Params {

    val originalColumn = SingleColumnSelectorParameter(
      name = OriginalColumnParameterName,
      description = Some("Column from the input DataFrame."),
      portIndex = 0
    )

    def getOriginalColumn: SingleColumnSelection = $(originalColumn)

    def setOriginalColumn(value: SingleColumnSelection): this.type = set(originalColumn, value)

    val renameColumn = ChoiceParameter[RenameColumnChoice](
      name = RenameColumnParameterName,
      description = Some("Determine if the column should be renamed.")
    )

    setDefault(renameColumn, RenameColumnChoice.No())

    def getRenameColumn: RenameColumnChoice = $(renameColumn)

    def setRenameColumn(value: RenameColumnChoice): this.type = set(renameColumn, value)

    val params: Array[ai.deepsense.deeplang.parameters.Parameter[_]] = Array(originalColumn, renameColumn)

  }

  sealed trait RenameColumnChoice extends Choice {

    import RenameColumnChoice._

    def getColumnName: Option[String]

    override val choiceOrder: List[Class[_ <: RenameColumnChoice]] =
      List(classOf[No], classOf[Yes])

  }

  object RenameColumnChoice {

    case class Yes() extends RenameColumnChoice {

      override val name: String = "Yes"

      val columnName = SingleColumnCreatorParameter(
        name = ColumnNameParameterName,
        description = Some("New name for a column in the output DataFrame.")
      )

      setDefault(columnName, "")

      override def getColumnName: Option[String] = Some($(columnName))

      def setColumnName(value: String): this.type = set(columnName, value)

      override val params: Array[Parameter[_]] = Array(columnName)

    }

    case class No() extends RenameColumnChoice {

      override val name: String = "No"

      override def getColumnName: Option[String] = None

      override val params: Array[Parameter[_]] = Array()

    }

  }

}
