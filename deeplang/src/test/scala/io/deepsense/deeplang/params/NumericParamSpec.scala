package io.deepsense.deeplang.params

import spray.json._

import io.deepsense.deeplang.params.validators.RangeValidator

class NumericParamSpec extends AbstractParamSpec[Double, NumericParam] {

  override def className: String = "NumericParam"

  override def paramFixture: (NumericParam, JsValue) = {
    val description = "Numeric parameter description"
    val param = NumericParam(
      name = "Numeric parameter",
      description = Some(description),
      validator = RangeValidator(1.0, 3.0, true, false)
    )
    val json = JsObject(
      "type"        -> JsString("numeric"),
      "name"        -> JsString(param.name),
      "description" -> JsString(description + param.constraints),
      "default"     -> JsNull,
      "isGriddable" -> JsTrue,
      "validator" -> JsObject(
        "type" -> JsString("range"),
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
