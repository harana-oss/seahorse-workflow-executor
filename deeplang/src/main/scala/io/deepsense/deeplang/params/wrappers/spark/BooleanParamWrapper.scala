package io.deepsense.deeplang.params.wrappers.spark

import org.apache.spark.ml

import io.deepsense.deeplang.params.BooleanParam

class BooleanParamWrapper[P <: ml.param.Params](
    override val name: String,
    override val description: Option[String],
    val sparkParamGetter: P => ml.param.Param[Boolean])
  extends BooleanParam(name, description)
  with ForwardSparkParamWrapper[P, Boolean] {

  override def replicate(name: String): BooleanParamWrapper[P] =
    new BooleanParamWrapper[P](name, description, sparkParamGetter)
}
