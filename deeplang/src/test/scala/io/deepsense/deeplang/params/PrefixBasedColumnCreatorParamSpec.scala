package io.deepsense.deeplang.params

import spray.json._

class PrefixBasedColumnCreatorParamSpec extends AbstractParamSpec[String, PrefixBasedColumnCreatorParam] {

  override def className: String = "PrefixBasedColumnCreatorParam"

  override def paramFixture: (PrefixBasedColumnCreatorParam, JsValue) = {
    val description = "Prefix based column creator description"
    val param = PrefixBasedColumnCreatorParam(
      name = "Prefix based column creator name",
      description = Some(description)
    )
    val expectedJson = JsObject(
      "type"        -> JsString("prefixBasedCreator"),
      "name"        -> JsString(param.name),
      "description" -> JsString(description),
      "isGriddable" -> JsFalse,
      "default"     -> JsNull
    )
    (param, expectedJson)
  }

  override def valueFixture: (String, JsValue) = {
    val value = "abc"
    (value, JsString(value))
  }

}
