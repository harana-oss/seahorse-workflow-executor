package io.deepsense.deeplang.params

import spray.json._
import spray.json.DefaultJsonProtocol._

class MultipleColumnCreatorParamSpec
  extends AbstractParamSpec[Array[String], MultipleColumnCreatorParam] {

  override def className: String = "MultipleColumnCreatorParam"

  override def paramFixture: (MultipleColumnCreatorParam, JsValue) = {
    val description = "Multiple column creator description"
    val param = MultipleColumnCreatorParam(
      name = "Multiple column creator name",
      description = Some(description)
    )
    val expectedJson = JsObject(
      "type" -> JsString("multipleCreator"),
      "name" -> JsString(param.name),
      "description" -> JsString(description),
      "isGriddable" -> JsFalse,
      "default" -> JsNull
    )
    (param, expectedJson)
  }

  override def valueFixture: (Array[String], JsValue) = {
    val value = Array("a", "b", "c")
    (value, value.toJson)
  }
}
