package io.deepsense.models.workflows

import spray.json.JsObject

import io.deepsense.commons.models
import io.deepsense.graph.DeeplangGraph

case class Workflow(
    metadata: WorkflowMetadata,
    graph: DeeplangGraph,
    additionalData: JsObject)

object Workflow {
  type Id = models.Id
  val Id = models.Id
}
