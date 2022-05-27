package ai.deepsense.models.workflows

import spray.json.JsObject

import ai.deepsense.commons.models
import ai.deepsense.graph.FlowGraph

case class Workflow(metadata: WorkflowMetadata, graph: FlowGraph, additionalData: JsObject)

object Workflow {

  type Id = models.Id

  val Id = models.Id

}
