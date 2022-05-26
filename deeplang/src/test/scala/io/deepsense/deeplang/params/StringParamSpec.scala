package io.deepsense.deeplang.params

import spray.json._

import io.deepsense.deeplang.params.validators.AcceptAllRegexValidator

class StringParamSpec extends AbstractParamSpec[String, StringParam] {

  override def className: String = "StringParam"

  override def paramFixture: (StringParam, JsValue) = {
    val description = "String parameter description"
    val param = StringParam(
      name = "String parameter name",
      description = Some(description),
      validator = new AcceptAllRegexValidator
    )
    val expectedJson = JsObject(
      "type"        -> JsString("string"),
      "name"        -> JsString(param.name),
      "description" -> JsString(description),
      "default"     -> JsNull,
      "isGriddable" -> JsFalse,
      "validator" -> JsObject(
        "type" -> JsString("regex"),
        "configuration" -> JsObject(
          "regex" -> JsString(".*")
        )
      )
    )
    (param, expectedJson)
  }

  override def valueFixture: (String, JsValue) = {
    val value = "abcdefghij"
    (value, JsString(value))
  }

}
