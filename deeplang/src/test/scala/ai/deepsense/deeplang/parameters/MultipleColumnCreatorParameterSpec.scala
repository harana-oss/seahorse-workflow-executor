package ai.deepsense.deeplang.parameters

import spray.json._
import spray.json.DefaultJsonProtocol._

class MultipleColumnCreatorParameterSpec extends AbstractParameterSpec[Array[String], MultipleColumnCreatorParameter] {

  override def className: String = "MultipleColumnCreatorParam"

  override def paramFixture: (MultipleColumnCreatorParameter, JsValue) = {
    val description  = "Multiple column creator description"
    val param        = MultipleColumnCreatorParameter(
      name = "Multiple column creator name",
      description = Some(description)
    )
    val expectedJson = JsObject(
      "type"        -> JsString("multipleCreator"),
      "name"        -> JsString(param.name),
      "description" -> JsString(description),
      "isGriddable" -> JsFalse,
      "default"     -> JsNull
    )
    (param, expectedJson)
  }

  override def valueFixture: (Array[String], JsValue) = {
    val value = Array("a", "b", "c")
    (value, value.toJson)
  }

}
