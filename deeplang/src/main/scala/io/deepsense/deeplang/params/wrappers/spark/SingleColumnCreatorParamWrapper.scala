package io.deepsense.deeplang.params.wrappers.spark

import org.apache.spark.ml

import io.deepsense.deeplang.params.SingleColumnCreatorParam

class SingleColumnCreatorParamWrapper[P <: ml.param.Params](
    override val name: String,
    override val description: Option[String],
    val sparkParamGetter: P => ml.param.Param[String]
) extends SingleColumnCreatorParam(name, description)
    with ForwardSparkParamWrapper[P, String] {

  override def replicate(name: String): SingleColumnCreatorParamWrapper[P] =
    new SingleColumnCreatorParamWrapper[P](name, description, sparkParamGetter)

}
