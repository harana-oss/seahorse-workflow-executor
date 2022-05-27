package ai.deepsense.deeplang.parameters.library

import spray.json._

import ai.deepsense.deeplang.parameters.AbstractParameterSpec

class LoadFromLibraryParamSpec extends AbstractParameterSpec[String, LoadFromLibraryParameter] {

  override def className: String = "LoadFromLibraryParam"

  override def paramFixture: (LoadFromLibraryParameter, JsValue) = {
    val description  = "Load parameter description"
    val param        = LoadFromLibraryParameter(name = "Load parameter name", description = Some(description))
    val expectedJson = JsObject(
      "type"        -> JsString("loadFromLibrary"),
      "name"        -> JsString(param.name),
      "description" -> JsString(description),
      "default"     -> JsNull,
      "isGriddable" -> JsFalse
    )
    (param, expectedJson)
  }

  override def valueFixture: (String, JsValue) = {
    val value = "input abcdefghij"
    (value, JsString(value))
  }

}
