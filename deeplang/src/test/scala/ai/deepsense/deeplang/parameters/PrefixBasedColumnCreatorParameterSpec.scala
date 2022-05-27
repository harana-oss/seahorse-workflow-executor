package ai.deepsense.deeplang.parameters

import spray.json._

class PrefixBasedColumnCreatorParameterSpec extends AbstractParameterSpec[String, PrefixBasedColumnCreatorParameter] {

  override def className: String = "PrefixBasedColumnCreatorParam"

  override def paramFixture: (PrefixBasedColumnCreatorParameter, JsValue) = {
    val description  = "Prefix based column creator description"
    val param        = PrefixBasedColumnCreatorParameter(
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
