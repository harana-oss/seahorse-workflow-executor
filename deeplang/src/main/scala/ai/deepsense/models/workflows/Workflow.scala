package ai.deepsense.models.workflows

import spray.json.JsObject

import ai.deepsense.commons.models
import ai.deepsense.graph.DeeplangGraph

case class Workflow(metadata: WorkflowMetadata, graph: DeeplangGraph, additionalData: JsObject)

object Workflow {

  type Id = models.Id

  val Id = models.Id

}
