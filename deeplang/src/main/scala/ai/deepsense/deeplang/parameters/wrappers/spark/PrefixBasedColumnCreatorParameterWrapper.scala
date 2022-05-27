package ai.deepsense.deeplang.parameters.wrappers.spark

import org.apache.spark.ml

import ai.deepsense.deeplang.parameters.PrefixBasedColumnCreatorParameter

class PrefixBasedColumnCreatorParameterWrapper[P <: ml.param.Params](
    override val name: String,
    override val description: Option[String],
    val sparkParamGetter: P => ml.param.Param[String]
) extends PrefixBasedColumnCreatorParameter(name, description)
    with ForwardSparkParameterWrapper[P, String] {

  override def replicate(name: String): PrefixBasedColumnCreatorParameterWrapper[P] =
    new PrefixBasedColumnCreatorParameterWrapper[P](name, description, sparkParamGetter)

}
