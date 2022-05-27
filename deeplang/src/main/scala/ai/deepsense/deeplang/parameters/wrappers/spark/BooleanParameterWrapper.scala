package ai.deepsense.deeplang.parameters.wrappers.spark

import org.apache.spark.ml

import ai.deepsense.deeplang.parameters.BooleanParameter

class BooleanParameterWrapper[P <: ml.param.Params](
    override val name: String,
    override val description: Option[String],
    val sparkParamGetter: P => ml.param.Param[Boolean]
) extends BooleanParameter(name, description)
    with ForwardSparkParameterWrapper[P, Boolean] {

  override def replicate(name: String): BooleanParameterWrapper[P] =
    new BooleanParameterWrapper[P](name, description, sparkParamGetter)

}
