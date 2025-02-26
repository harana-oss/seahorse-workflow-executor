package ai.deepsense.deeplang.actionobjects

import org.apache.spark.sql.types.StructType

import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.actionobjects.dataframe.DataFrameColumnsGetter
import ai.deepsense.deeplang.parameters.selections.MultipleColumnSelection
import ai.deepsense.deeplang.parameters.selections.NameColumnSelection
import ai.deepsense.deeplang.parameters.ColumnSelectorParameter
import ai.deepsense.deeplang.parameters.Parameter

class ColumnsFilterer extends Transformer {

  val selectedColumns = ColumnSelectorParameter(
    name = "selected columns",
    description = Some("Columns to be retained in the output DataFrame."),
    portIndex = 0
  )

  def getSelectedColumns: MultipleColumnSelection = $(selectedColumns)

  def setSelectedColumns(value: MultipleColumnSelection): this.type =
    set(selectedColumns, value)

  def setSelectedColumns(retainedColumns: Seq[String]): this.type =
    setSelectedColumns(MultipleColumnSelection(Vector(NameColumnSelection(retainedColumns.toSet)), excluding = false))

  override val params: Array[Parameter[_]] = Array(selectedColumns)

  override def applyTransform(ctx: ExecutionContext, df: DataFrame): DataFrame = {
    val columns = df.getColumnNames(getSelectedColumns)
    if (columns.isEmpty)
      DataFrame.empty(ctx)
    else {
      val filtered = df.sparkDataFrame.select(columns.head, columns.tail: _*)
      DataFrame.fromSparkDataFrame(filtered)
    }
  }

  override def applyTransformSchema(schema: StructType): Option[StructType] = {
    val outputColumns  = DataFrameColumnsGetter.getColumnNames(schema, getSelectedColumns)
    val inferredSchema =
      if (outputColumns.isEmpty)
        StructType(Seq.empty)
      else {
        val fields = schema.filter(field => outputColumns.contains(field.name))
        StructType(fields)
      }
    Some(inferredSchema)
  }

}
