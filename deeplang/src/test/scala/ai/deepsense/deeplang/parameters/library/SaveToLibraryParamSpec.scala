package ai.deepsense.deeplang.parameters.library

import spray.json._

import ai.deepsense.deeplang.parameters.AbstractParameterSpec

class SaveToLibraryParamSpec extends AbstractParameterSpec[String, SaveToLibraryParameter] {

  override def className: String = "SaveToLibraryParam"

  override def paramFixture: (SaveToLibraryParameter, JsValue) = {
    val description  = "Save parameter description"
    val param        = SaveToLibraryParameter(name = "Save parameter name", description = Some(description))
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
