package ai.deepsense.deeplang.parameters

import spray.json.DefaultJsonProtocol.StringJsonFormat

import ai.deepsense.deeplang.parameters.validators.ColumnNameValidator
import ai.deepsense.deeplang.parameters.validators.Validator

case class SingleColumnCreatorParameter(override val name: String, override val description: Option[String])
    extends ParameterWithJsFormat[String]
    with HasValidator[String] {

  val validator: Validator[String] = ColumnNameValidator

  val parameterType = ParameterType.SingleColumnCreator

  override def replicate(name: String): SingleColumnCreatorParameter = copy(name = name)

}
