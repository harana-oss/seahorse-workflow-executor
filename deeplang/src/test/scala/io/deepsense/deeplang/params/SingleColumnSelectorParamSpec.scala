package io.deepsense.deeplang.params

import spray.json._

import io.deepsense.deeplang.params.selections.{IndexSingleColumnSelection, SingleColumnSelection}

class SingleColumnSelectorParamSpec
  extends AbstractParamSpec[SingleColumnSelection, SingleColumnSelectorParam] {

  override def className: String = "SingleColumnSelectorParam"

  override def paramFixture: (SingleColumnSelectorParam, JsValue) = {
    val description = "Single column selector description"
    val param = SingleColumnSelectorParam(
      name = "Single column selector name",
      description = Some(description),
      portIndex = 0)
    val expectedJson = JsObject(
      "type" -> JsString("selector"),
      "name" -> JsString(param.name),
      "description" -> JsString(description),
      "portIndex" -> JsNumber(param.portIndex),
      "isSingle" -> JsTrue,
      "isGriddable" -> JsFalse,
      "default" -> JsNull
    )
    (param, expectedJson)
  }

  override def valueFixture: (SingleColumnSelection, JsValue) = {
    val selection = IndexSingleColumnSelection(2)
    val expectedJson = JsObject(
      "type" -> JsString("index"),
      "value" -> JsNumber(2)
    )
    (selection, expectedJson)
  }
}
