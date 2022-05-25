package io.deepsense.deeplang.params.wrappers.spark

import org.apache.spark.ml
import org.apache.spark.sql.types.StructType

import io.deepsense.deeplang.doperables.dataframe.DataFrameColumnsGetter
import io.deepsense.deeplang.params.SingleColumnSelectorParam
import io.deepsense.deeplang.params.selections.SingleColumnSelection

class SingleColumnSelectorParamWrapper[P <: ml.param.Params](
    override val name: String,
    override val description: Option[String],
    val sparkParamGetter: P => ml.param.Param[String],
    override val portIndex: Int)
  extends SingleColumnSelectorParam(name, description, portIndex)
  with SparkParamWrapper[P, String, SingleColumnSelection] {

  override def convert(value: SingleColumnSelection)(schema: StructType): String =
    DataFrameColumnsGetter.getColumnName(schema, value)

  override def replicate(name: String): SingleColumnSelectorParamWrapper[P] =
    new SingleColumnSelectorParamWrapper[P](name, description, sparkParamGetter, portIndex)
}
