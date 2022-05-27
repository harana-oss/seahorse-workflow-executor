package ai.deepsense.deeplang.parameters.wrappers.spark

import org.apache.spark.ml
import org.apache.spark.sql.types.StructType

import ai.deepsense.deeplang.actionobjects.dataframe.DataFrameColumnsGetter
import ai.deepsense.deeplang.parameters.SingleColumnSelectorParameter
import ai.deepsense.deeplang.parameters.selections.SingleColumnSelection

class SingleColumnSelectorParameterWrapper[P <: ml.param.Params](
    override val name: String,
    override val description: Option[String],
    val sparkParamGetter: P => ml.param.Param[String],
    override val portIndex: Int
) extends SingleColumnSelectorParameter(name, description, portIndex)
    with SparkParameterWrapper[P, String, SingleColumnSelection] {

  override def convert(value: SingleColumnSelection)(schema: StructType): String =
    DataFrameColumnsGetter.getColumnName(schema, value)

  override def replicate(name: String): SingleColumnSelectorParameterWrapper[P] =
    new SingleColumnSelectorParameterWrapper[P](name, description, sparkParamGetter, portIndex)

}
