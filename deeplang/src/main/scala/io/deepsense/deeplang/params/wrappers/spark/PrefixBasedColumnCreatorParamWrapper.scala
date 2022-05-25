package io.deepsense.deeplang.params.wrappers.spark

import org.apache.spark.ml

import io.deepsense.deeplang.params.PrefixBasedColumnCreatorParam

class PrefixBasedColumnCreatorParamWrapper[P <: ml.param.Params](
    override val name: String,
    override val description: Option[String],
    val sparkParamGetter: P => ml.param.Param[String])
  extends PrefixBasedColumnCreatorParam(name, description)
  with ForwardSparkParamWrapper[P, String] {

  override def replicate(name: String): PrefixBasedColumnCreatorParamWrapper[P] =
    new PrefixBasedColumnCreatorParamWrapper[P](name, description, sparkParamGetter)
}
