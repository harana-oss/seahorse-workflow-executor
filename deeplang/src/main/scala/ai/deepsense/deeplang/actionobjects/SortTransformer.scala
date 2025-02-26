package ai.deepsense.deeplang.actionobjects

import org.apache.spark.sql.Column
import org.apache.spark.sql.functions.col
import org.apache.spark.sql.types.StructType

import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.actionobjects.dataframe.DataFrameColumnsGetter
import ai.deepsense.deeplang.parameters._
import ai.deepsense.deeplang.parameters.selections.IndexSingleColumnSelection
import ai.deepsense.deeplang.parameters.selections.NameSingleColumnSelection
import ai.deepsense.deeplang.parameters.selections.SingleColumnSelection

/** Sorts the input [[ai.deepsense.deeplang.actionobjects.dataframe.DataFrame Dataframe]] according to selected columns. */
class SortTransformer extends Transformer {

  val columns = ParamsSequence[SortColumnParam](
    name = "sort columns",
    description = Some("Columns that will be used to sort the DataFrame.")
  )

  def getColumns: Seq[SortColumnParam] = $(columns)

  def setColumns(sortColumnParams: Seq[SortColumnParam]): this.type =
    set(columns, sortColumnParams)

  override protected def applyTransform(ctx: ExecutionContext, df: DataFrame): DataFrame = {
    getColumns match {
      case Nil             => df // Sort in Spark 2.0 is no-op for empty columns, but in 1.6 it throws. Here we always do no-op.
      case selectedColumns =>
        DataFrame.fromSparkDataFrame(
          df.sparkDataFrame.sort($(columns).map(SortColumnParam.columnParamToColumnExpression(_, df)): _*)
        )
    }
  }

  override def params: Array[Parameter[_]] = Array(columns)

  override protected def applyTransformSchema(schema: StructType): Option[StructType] = {
    // Check that all columns selected for sorting exist
    getSelectedSortColumnNames(schema, _.getColumn)
    Some(schema)
  }

  private def getSelectedSortColumnNames(
      schema: StructType,
      selector: SortColumnParam => SingleColumnSelection
  ): Seq[String] =
    getColumns.map(columnPair => DataFrameColumnsGetter.getColumnName(schema, selector(columnPair)))

}

class SortColumnParam extends Params {

  import SortColumnParam._

  val column = SingleColumnSelectorParameter(
    name = columnNameParamName,
    description = None,
    portIndex = 0
  )

  val descending = BooleanParameter(
    name = descendingFlagParamName,
    description = Some("Should sort in descending order?")
  )

  setDefault(descending, false)

  def getDescending: Boolean = $(descending)

  def isDescending: Boolean = getDescending

  def setDescending(desc: Boolean): this.type = set(descending, desc)

  def getColumn: SingleColumnSelection = $(column)

  def setColumn(col: SingleColumnSelection): this.type = set(column, col)

  override def params: Array[Parameter[_]] = Array(column, descending)

}

object SortColumnParam {

  val columnNameParamName = "column name"

  val descendingFlagParamName = "descending"

  def columnParamToColumnExpression(scp: SortColumnParam, df: DataFrame): Column = {
    val column = col(DataFrameColumnsGetter.getColumnName(df.schema.get, scp.getColumn))
    if (scp.getDescending)
      column.desc
    else
      column.asc
  }

  def apply(columnName: String, descending: Boolean): SortColumnParam = {
    new SortColumnParam()
      .setColumn(new NameSingleColumnSelection(columnName))
      .setDescending(descending)
  }

  def apply(columnIndex: Int, descending: Boolean): SortColumnParam = {
    new SortColumnParam()
      .setColumn(new IndexSingleColumnSelection(columnIndex))
      .setDescending(descending)
  }

  def apply(columnSelection: SingleColumnSelection, descending: Boolean): SortColumnParam = {
    new SortColumnParam()
      .setColumn(columnSelection)
      .setDescending(descending)
  }

}
