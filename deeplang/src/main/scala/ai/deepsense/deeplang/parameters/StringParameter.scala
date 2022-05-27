package ai.deepsense.deeplang.parameters

import spray.json.DefaultJsonProtocol.StringJsonFormat

import ai.deepsense.deeplang.parameters.validators.AcceptAllRegexValidator
import ai.deepsense.deeplang.parameters.validators.Validator

case class StringParameter(
    override val name: String,
    override val description: Option[String],
    override val validator: Validator[String] = new AcceptAllRegexValidator
) extends ParameterWithJsFormat[String]
    with HasValidator[String] {

  override val parameterType = ParameterType.String

  override def replicate(name: String): StringParameter = copy(name = name)

}
