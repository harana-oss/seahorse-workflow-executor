package io.deepsense.deeplang.doperables

import org.apache.spark.sql.types.StructType

import io.deepsense.deeplang.ExecutionContext
import io.deepsense.deeplang.doperables.dataframe.DataFrame
import io.deepsense.deeplang.doperables.dataframe.DataFrameColumnsGetter
import io.deepsense.deeplang.params.selections.MultipleColumnSelection
import io.deepsense.deeplang.params.selections.NameColumnSelection
import io.deepsense.deeplang.params.ColumnSelectorParam
import io.deepsense.deeplang.params.Param

class ColumnsFilterer extends Transformer {

  val selectedColumns = ColumnSelectorParam(
    name = "selected columns",
    description = Some("Columns to be retained in the output DataFrame."),
    portIndex = 0
  )

  def getSelectedColumns: MultipleColumnSelection = $(selectedColumns)

  def setSelectedColumns(value: MultipleColumnSelection): this.type =
    set(selectedColumns, value)

  def setSelectedColumns(retainedColumns: Seq[String]): this.type =
    setSelectedColumns(MultipleColumnSelection(Vector(NameColumnSelection(retainedColumns.toSet)), excluding = false))

  override val params: Array[Param[_]] = Array(selectedColumns)

  override def _transform(ctx: ExecutionContext, df: DataFrame): DataFrame = {
    val columns = df.getColumnNames(getSelectedColumns)
    if (columns.isEmpty)
      DataFrame.empty(ctx)
    else {
      val filtered = df.sparkDataFrame.select(columns.head, columns.tail: _*)
      DataFrame.fromSparkDataFrame(filtered)
    }
  }

  override def _transformSchema(schema: StructType): Option[StructType] = {
    val outputColumns = DataFrameColumnsGetter.getColumnNames(schema, getSelectedColumns)
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
