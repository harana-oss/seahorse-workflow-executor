package io.deepsense.deeplang.params

import spray.json._

import io.deepsense.deeplang.params.selections.{ColumnSelection, MultipleColumnSelection, NameColumnSelection}

class ColumnSelectorParamSpec
  extends AbstractParamSpec[MultipleColumnSelection, ColumnSelectorParam] {

  override def className: String = "MultipleColumnCreatorParam"

  override def paramFixture: (ColumnSelectorParam, JsValue) = {
    val description = "Column selector description"
    val param = ColumnSelectorParam(
      name = "Column selector name",
      description = Some(description),
      portIndex = 0)
    val expectedJson = JsObject(
      "type" -> JsString("selector"),
      "name" -> JsString(param.name),
      "description" -> JsString(description),
      "portIndex" -> JsNumber(param.portIndex),
      "isSingle" -> JsFalse,
      "isGriddable" -> JsFalse,
      "default" -> JsNull
    )
    (param, expectedJson)
  }

  override def valueFixture: (MultipleColumnSelection, JsValue) = {
    val value = MultipleColumnSelection(
      selections = Vector[ColumnSelection](
        NameColumnSelection(Set("a", "b"))
      ),
      excluding = false)
    val expectedJson = JsObject(
      "selections" -> JsArray(
        JsObject(
          "type" -> JsString("columnList"),
          "values" -> JsArray(JsString("a"), JsString("b"))
        )
      ),
      "excluding" -> JsFalse
    )
    (value, expectedJson)
  }
}
