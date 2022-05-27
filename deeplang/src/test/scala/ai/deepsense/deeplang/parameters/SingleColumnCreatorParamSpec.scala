package ai.deepsense.deeplang.parameters

import spray.json._

import ai.deepsense.deeplang.parameters.validators.ColumnNameValidator

class SingleColumnCreatorParamSpec extends AbstractParameterSpec[String, SingleColumnCreatorParameter] {

  override def className: String = "SingleColumnCreatorParam"

  override def paramFixture: (SingleColumnCreatorParameter, JsValue) = {
    val description  = "Single column creator description"
    val param        = SingleColumnCreatorParameter(name = "Single column creator name", description = Some(description))
    val expectedJson = JsObject(
      "type"        -> JsString("creator"),
      "name"        -> JsString(param.name),
      "description" -> JsString(description),
      "isGriddable" -> JsFalse,
      "default"     -> JsNull,
      "validator"   -> JsObject(
        "type"          -> JsString("regex"),
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
