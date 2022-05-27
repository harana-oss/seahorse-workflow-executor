package ai.deepsense.deeplang.parameters.validators

import spray.json._

import ai.deepsense.deeplang.exceptions.DeepLangException
import ai.deepsense.deeplang.parameters.validators.ValidatorType.ValidatorType

/** Represents anything that validates parameter. */
@SerialVersionUID(1)
trait Validator[ParameterType] extends Serializable {

  val validatorType: ValidatorType

  def validate(name: String, parameter: ParameterType): Vector[DeepLangException]

  final def toJson: JsObject = {
    import DefaultJsonProtocol._
    JsObject("type" -> validatorType.toString.toJson, "configuration" -> configurationToJson)
  }

  def toHumanReadable(paramName: String): String = ""

  protected def configurationToJson: JsObject

}
