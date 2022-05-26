package io.deepsense.deeplang.params

import spray.json.JsValue
import spray.json.JsObject

import io.deepsense.deeplang.exceptions.DeepLangException

case class WorkflowParam(override val name: String, override val description: Option[String]) extends Param[JsObject] {

  override val parameterType = ParameterType.Workflow

  override def valueToJson(value: JsObject): JsValue = value

  override def valueFromJson(jsValue: JsValue): JsObject = jsValue.asJsObject

  override def validate(value: JsObject): Vector[DeepLangException] =
    super.validate(value)
  // TODO: validate if json is a valid workflow representation.

  override def replicate(name: String): WorkflowParam = copy(name = name)

}
