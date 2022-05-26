package ai.deepsense.deeplang.params.library

import spray.json._

import ai.deepsense.deeplang.params.AbstractParamSpec

class SaveToLibraryParamSpec extends AbstractParamSpec[String, SaveToLibraryParam] {

  override def className: String = "SaveToLibraryParam"

  override def paramFixture: (SaveToLibraryParam, JsValue) = {
    val description  = "Save parameter description"
    val param        = SaveToLibraryParam(name = "Save parameter name", description = Some(description))
    val expectedJson = JsObject(
      "type"        -> JsString("saveToLibrary"),
      "name"        -> JsString(param.name),
      "description" -> JsString(description),
      "default"     -> JsNull,
      "isGriddable" -> JsFalse
    )
    (param, expectedJson)
  }

  override def valueFixture: (String, JsValue) = {
    val value = "output abcdefghij"
    (value, JsString(value))
  }

}
