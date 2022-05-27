package ai.deepsense.deeplang.parameters

import spray.json._

class BooleanParameterSpec extends AbstractParameterSpec[Boolean, BooleanParameter] {

  override def className: String = "BooleanParam"

  override def paramFixture: (BooleanParameter, JsValue) = {
    val description = "Boolean param description"
    val param       = BooleanParameter(name = "Boolean param name", description = Some(description))
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
