package io.deepsense.deeplang.params.library

import spray.json._

import io.deepsense.deeplang.params.AbstractParamSpec

class LoadFromLibraryParamSpec extends AbstractParamSpec[String, LoadFromLibraryParam] {

  override def className: String = "LoadFromLibraryParam"

  override def paramFixture: (LoadFromLibraryParam, JsValue) = {
    val description = "Load parameter description"
    val param = LoadFromLibraryParam(
      name = "Load parameter name",
      description = Some(description))
    val expectedJson = JsObject(
      "type" -> JsString("loadFromLibrary"),
      "name" -> JsString(param.name),
      "description" -> JsString(description),
      "default" -> JsNull,
      "isGriddable" -> JsFalse
    )
    (param, expectedJson)
  }

  override def valueFixture: (String, JsValue) = {
    val value = "input abcdefghij"
    (value, JsString(value))
  }
}
