package ai.deepsense.deeplang.parameters

import spray.json._

import ai.deepsense.deeplang.parameters.validators.AcceptAllRegexValidator

class StringParamSpec extends AbstractParameterSpec[String, StringParameter] {

  override def className: String = "StringParam"

  override def paramFixture: (StringParameter, JsValue) = {
    val description  = "String parameter description"
    val param        = StringParameter(
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
      "validator"   -> JsObject(
        "type"          -> JsString("regex"),
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
