package ai.deepsense.deeplang.parameters.wrappers.spark

import org.apache.spark.ml
import org.apache.spark.sql.types.StructType

import ai.deepsense.deeplang.actionobjects.dataframe.DataFrameColumnsGetter
import ai.deepsense.deeplang.parameters.ColumnSelectorParameter
import ai.deepsense.deeplang.parameters.selections.MultipleColumnSelection

class ColumnSelectorParameterWrapper[P <: ml.param.Params](
    override val name: String,
    override val description: Option[String],
    val sparkParamGetter: P => ml.param.StringArrayParam,
    override val portIndex: Int = 0
) extends ColumnSelectorParameter(name, description, portIndex)
    with SparkParameterWrapper[P, Array[String], MultipleColumnSelection] {

  override def convert(value: MultipleColumnSelection)(schema: StructType): Array[String] =
    DataFrameColumnsGetter.getColumnNames(schema, value).toArray

  override def replicate(name: String): ColumnSelectorParameterWrapper[P] =
    new ColumnSelectorParameterWrapper[P](name, description, sparkParamGetter, portIndex)

}
