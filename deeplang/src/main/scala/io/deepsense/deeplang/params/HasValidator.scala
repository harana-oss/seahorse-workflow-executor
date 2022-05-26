package io.deepsense.deeplang.params

import spray.json.JsValue

import io.deepsense.deeplang.exceptions.DeepLangException
import io.deepsense.deeplang.params.validators.Validator

trait HasValidator[T] extends Param[T] {

  val validator: Validator[T]

  override def validate(value: T): Vector[DeepLangException] = validator.validate(name, value)

  override def constraints: String =
    if (validator.toHumanReadable(name).isEmpty)
      ""
    else
      " " + validator.toHumanReadable(name)

  override protected def extraJsFields: Map[String, JsValue] =
    super.extraJsFields ++ Map("validator" -> validator.toJson)

}
