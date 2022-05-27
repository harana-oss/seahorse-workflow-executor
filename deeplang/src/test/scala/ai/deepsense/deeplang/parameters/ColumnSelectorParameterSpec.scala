package ai.deepsense.deeplang.parameters

import spray.json._

import ai.deepsense.deeplang.parameters.selections.ColumnSelection
import ai.deepsense.deeplang.parameters.selections.MultipleColumnSelection
import ai.deepsense.deeplang.parameters.selections.NameColumnSelection

class ColumnSelectorParameterSpec extends AbstractParameterSpec[MultipleColumnSelection, ColumnSelectorParameter] {

  override def className: String = "MultipleColumnCreatorParam"

  override def paramFixture: (ColumnSelectorParameter, JsValue) = {
    val description  = "Column selector description"
    val param        = ColumnSelectorParameter(name = "Column selector name", description = Some(description), portIndex = 0)
    val expectedJson = JsObject(
      "type"        -> JsString("selector"),
      "name"        -> JsString(param.name),
      "description" -> JsString(description),
      "portIndex"   -> JsNumber(param.portIndex),
      "isSingle"    -> JsFalse,
      "isGriddable" -> JsFalse,
      "default"     -> JsNull
    )
    (param, expectedJson)
  }

  override def valueFixture: (MultipleColumnSelection, JsValue) = {
    val value        = MultipleColumnSelection(
      selections = Vector[ColumnSelection](
        NameColumnSelection(Set("a", "b"))
      ),
      excluding = false
    )
    val expectedJson = JsObject(
      "selections" -> JsArray(
        JsObject(
          "type"   -> JsString("columnList"),
          "values" -> JsArray(JsString("a"), JsString("b"))
        )
      ),
      "excluding"  -> JsFalse
    )
    (value, expectedJson)
  }

}
