package io.deepsense.deeplang.params.wrappers.spark

import org.apache.spark.ml

import io.deepsense.deeplang.params.MultipleColumnCreatorParam

class MultipleColumnCreatorParamWrapper[P <: ml.param.Params](
    override val name: String,
    override val description: Option[String],
    val sparkParamGetter: P => ml.param.StringArrayParam)
  extends MultipleColumnCreatorParam(name, description)
  with ForwardSparkParamWrapper[P, Array[String]] {

  override def replicate(name: String): MultipleColumnCreatorParamWrapper[P] =
    new MultipleColumnCreatorParamWrapper[P](name, description, sparkParamGetter)
}
