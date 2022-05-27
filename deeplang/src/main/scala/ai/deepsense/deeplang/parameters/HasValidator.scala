package ai.deepsense.deeplang.parameters

import spray.json.JsValue

import ai.deepsense.deeplang.exceptions.FlowException
import ai.deepsense.deeplang.parameters.validators.Validator

trait HasValidator[T] extends Parameter[T] {

  val validator: Validator[T]

  override def validate(value: T): Vector[FlowException] = validator.validate(name, value)

  override def constraints: String =
    if (validator.toHumanReadable(name).isEmpty)
      ""
    else
      " " + validator.toHumanReadable(name)

  override protected def extraJsFields: Map[String, JsValue] =
    super.extraJsFields ++ Map("validator" -> validator.toJson)

}
