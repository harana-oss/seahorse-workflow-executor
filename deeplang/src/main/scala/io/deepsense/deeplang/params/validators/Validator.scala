package io.deepsense.deeplang.params.validators

import spray.json._

import io.deepsense.deeplang.exceptions.DeepLangException
import io.deepsense.deeplang.params.validators.ValidatorType.ValidatorType

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
