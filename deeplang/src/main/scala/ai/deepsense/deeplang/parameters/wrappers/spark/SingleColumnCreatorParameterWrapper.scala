package ai.deepsense.deeplang.parameters.wrappers.spark

import org.apache.spark.ml

import ai.deepsense.deeplang.parameters.SingleColumnCreatorParameter

class SingleColumnCreatorParameterWrapper[P <: ml.param.Params](
    override val name: String,
    override val description: Option[String],
    val sparkParamGetter: P => ml.param.Param[String]
) extends SingleColumnCreatorParameter(name, description)
    with ForwardSparkParameterWrapper[P, String] {

  override def replicate(name: String): SingleColumnCreatorParameterWrapper[P] =
    new SingleColumnCreatorParameterWrapper[P](name, description, sparkParamGetter)

}
