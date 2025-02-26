package ai.deepsense.deeplang.parameters

import spray.json._

import ai.deepsense.models.json.graph.GraphJsonProtocol.GraphReader

class DynamicParamSpec extends AbstractParameterSpec[JsValue, DynamicParameter] {

  override def className: String = "DynamicParam"

  override def paramFixture: (DynamicParameter, JsValue) = {
    val description = "Dynamic param description"
    val param       = new DynamicParameter("Dynamic param name", Some(description), inputPort = 4)
    val json        = JsObject(
      "type"        -> JsString("dynamic"),
      "name"        -> JsString(param.name),
      "description" -> JsString(description),
      "inputPort"   -> JsNumber(param.inputPort),
      "isGriddable" -> JsFalse,
      "default"     -> JsNull
    )
    (param, json)
  }

  override def valueFixture: (JsValue, JsValue) = {
    val anyJson = JsObject("a" -> JsNumber(3), "b" -> JsString("c"))
    (anyJson, anyJson)
  }

  it should {
    "skip JsNull values" in {
      val (param, _)  = paramFixture
      val input       = JsObject("a" -> JsNumber(3), "b" -> JsNull)
      val expected    = JsObject("a" -> JsNumber(3))
      val graphReader = mock[GraphReader]
      param.valueFromJson(input, graphReader) shouldBe expected
    }
  }

}
