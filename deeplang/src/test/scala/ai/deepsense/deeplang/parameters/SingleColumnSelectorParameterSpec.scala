package ai.deepsense.deeplang.parameters

import spray.json._

import ai.deepsense.deeplang.parameters.selections.IndexSingleColumnSelection
import ai.deepsense.deeplang.parameters.selections.SingleColumnSelection

class SingleColumnSelectorParameterSpec extends AbstractParameterSpec[SingleColumnSelection, SingleColumnSelectorParameter] {

  override def className: String = "SingleColumnSelectorParam"

  override def paramFixture: (SingleColumnSelectorParameter, JsValue) = {
    val description  = "Single column selector description"
    val param        =
      SingleColumnSelectorParameter(name = "Single column selector name", description = Some(description), portIndex = 0)
    val expectedJson = JsObject(
      "type"        -> JsString("selector"),
      "name"        -> JsString(param.name),
      "description" -> JsString(description),
      "portIndex"   -> JsNumber(param.portIndex),
      "isSingle"    -> JsTrue,
      "isGriddable" -> JsFalse,
      "default"     -> JsNull
    )
    (param, expectedJson)
  }

  override def valueFixture: (SingleColumnSelection, JsValue) = {
    val selection    = IndexSingleColumnSelection(2)
    val expectedJson = JsObject(
      "type"  -> JsString("index"),
      "value" -> JsNumber(2)
    )
    (selection, expectedJson)
  }

}
