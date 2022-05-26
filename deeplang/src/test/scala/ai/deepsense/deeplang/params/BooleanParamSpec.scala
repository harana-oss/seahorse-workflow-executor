package ai.deepsense.deeplang.params

import spray.json._

class BooleanParamSpec extends AbstractParamSpec[Boolean, BooleanParam] {

  override def className: String = "BooleanParam"

  override def paramFixture: (BooleanParam, JsValue) = {
    val description = "Boolean param description"
    val param       = BooleanParam(name = "Boolean param name", description = Some(description))
    val json        = JsObject(
      "type"        -> JsString("boolean"),
      "name"        -> JsString(param.name),
      "description" -> JsString(description),
      "isGriddable" -> JsFalse,
      "default"     -> JsNull
    )
    (param, json)
  }

  override def valueFixture: (Boolean, JsValue) = (true, JsBoolean(true))

}
