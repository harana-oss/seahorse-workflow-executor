package io.deepsense.deeplang.params

import spray.json._

import io.deepsense.deeplang.params.validators.ColumnNameValidator

class SingleColumnCreatorParamSpec extends AbstractParamSpec[String, SingleColumnCreatorParam] {

  override def className: String = "SingleColumnCreatorParam"

  override def paramFixture: (SingleColumnCreatorParam, JsValue) = {
    val description = "Single column creator description"
    val param = SingleColumnCreatorParam(
      name = "Single column creator name",
      description = Some(description))
    val expectedJson = JsObject(
      "type" -> JsString("creator"),
      "name" -> JsString(param.name),
      "description" -> JsString(description),
      "isGriddable" -> JsFalse,
      "default" -> JsNull,
      "validator" -> JsObject(
        "type" -> JsString("regex"),
        "configuration" -> JsObject(
          "regex" -> JsString(ColumnNameValidator.regex.toString())
        )
      )
    )
    (param, expectedJson)
  }

  override def valueFixture: (String, JsValue) = {
    val value = "abc"
    (value, JsString(value))
  }
}
