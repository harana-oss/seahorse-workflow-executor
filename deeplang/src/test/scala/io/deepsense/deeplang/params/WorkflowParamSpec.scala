package io.deepsense.deeplang.params

import spray.json._

class WorkflowParamSpec extends AbstractParamSpec[JsObject, WorkflowParam] {

  override def className: String = "WorkflowParam"

  override def paramFixture: (WorkflowParam, JsValue) = {
    val description = "Workflow parameter description"
    val param       = WorkflowParam(name = "Workflow parameter name", description = Some(description))
    val expectedJson = JsObject(
      "type"        -> JsString("workflow"),
      "name"        -> JsString(param.name),
      "description" -> JsString(description),
      "isGriddable" -> JsFalse,
      "default"     -> JsNull
    )
    (param, expectedJson)
  }

  override def valueFixture: (JsObject, JsValue) = {
    val value = JsObject(
      "field" -> JsString("value"),
      "array" -> JsArray(
        JsString("one"),
        JsString("two"),
        JsString("three")
      ),
      "object" -> JsObject(
        "inner" -> JsString("value")
      )
    )
    (value, value.copy())
  }

}
