package ai.deepsense.models.workflows

import spray.json.JsObject

import ai.deepsense.graph.DeeplangGraph

case class WorkflowWithVariables(
    id: Workflow.Id,
    metadata: WorkflowMetadata,
    graph: DeeplangGraph,
    thirdPartyData: JsObject,
    variables: Variables
)
