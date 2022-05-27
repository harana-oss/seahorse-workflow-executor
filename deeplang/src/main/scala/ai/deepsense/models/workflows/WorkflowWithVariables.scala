package ai.deepsense.models.workflows

import spray.json.JsObject

import ai.deepsense.graph.FlowGraph

case class WorkflowWithVariables(
                                  id: Workflow.Id,
                                  metadata: WorkflowMetadata,
                                  graph: FlowGraph,
                                  thirdPartyData: JsObject,
                                  variables: Variables
)
