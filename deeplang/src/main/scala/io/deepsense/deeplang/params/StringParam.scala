package io.deepsense.deeplang.params

import spray.json.DefaultJsonProtocol.StringJsonFormat

import io.deepsense.deeplang.params.validators.AcceptAllRegexValidator
import io.deepsense.deeplang.params.validators.Validator

case class StringParam(
    override val name: String,
    override val description: Option[String],
    override val validator: Validator[String] = new AcceptAllRegexValidator
) extends ParamWithJsFormat[String]
    with HasValidator[String] {

  override val parameterType = ParameterType.String

  override def replicate(name: String): StringParam = copy(name = name)

}
