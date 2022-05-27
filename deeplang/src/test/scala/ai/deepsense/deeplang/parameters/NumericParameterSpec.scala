package ai.deepsense.deeplang.parameters

import spray.json._

import ai.deepsense.deeplang.parameters.validators.RangeValidator

class NumericParameterSpec extends AbstractParameterSpec[Double, NumericParameter] {

  override def className: String = "NumericParam"

  override def paramFixture: (NumericParameter, JsValue) = {
    val description = "Numeric parameter description"
    val param       = NumericParameter(
      name = "Numeric parameter",
      description = Some(description),
      validator = RangeValidator(1.0, 3.0, true, false)
    )
    val json        = JsObject(
      "type"        -> JsString("numeric"),
      "name"        -> JsString(param.name),
      "description" -> JsString(description + param.constraints),
      "default"     -> JsNull,
      "isGriddable" -> JsTrue,
      "validator"   -> JsObject(
        "type"          -> JsString("range"),
        "configuration" -> JsObject(
          "begin"         -> JsNumber(1.0),
          "end"           -> JsNumber(3.0),
          "beginIncluded" -> JsBoolean(true),
          "endIncluded"   -> JsBoolean(false)
        )
      )
    )
    (param, json)
  }

  override def valueFixture: (Double, JsValue) = {
    val value = 2.5
    (value, JsNumber(value))
  }

}
