package io.deepsense.workflowexecutor

import spray.json.lenses.JsonLenses._
import spray.json.DefaultJsonProtocol
import spray.json.JsValue

object WorkflowJsonParamsOverrider extends DefaultJsonProtocol {

  def overrideParams(workflow: JsValue, params: Map[String, String]): JsValue =
    params.foldLeft(workflow)(overrideParam)

  private def overrideParam(json: JsValue, param: (String, String)): JsValue = {
    val (key, value) = param
    val pathElems    = key.split("\\.")
    val basePath =
      "workflow" / "nodes" / filter("id".is[String](_ == pathElems(0))) / "parameters"
    val path = pathElems.drop(1).foldLeft(basePath)((a, b) => a / b)
    json.update(path ! modify[String](_ => value))
  }

}
