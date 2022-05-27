package ai.deepsense.deeplang.parameters.wrappers.spark

import org.apache.spark.ml

import ai.deepsense.deeplang.parameters.StringParameter
import ai.deepsense.deeplang.parameters.validators.AcceptAllRegexValidator
import ai.deepsense.deeplang.parameters.validators.Validator

class StringParameterWrapper[P <: ml.param.Params](
    override val name: String,
    override val description: Option[String],
    val sparkParamGetter: P => ml.param.Param[String],
    override val validator: Validator[String] = new AcceptAllRegexValidator
) extends StringParameter(name, description, validator)
    with ForwardSparkParameterWrapper[P, String] {

  override def replicate(name: String): StringParameterWrapper[P] =
    new StringParameterWrapper[P](name, description, sparkParamGetter, validator)

}
