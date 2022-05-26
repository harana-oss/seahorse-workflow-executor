package io.deepsense.models.workflows

import spray.json.JsObject

import io.deepsense.graph.DeeplangGraph

case class WorkflowWithVariables(
    id: Workflow.Id,
    metadata: WorkflowMetadata,
    graph: DeeplangGraph,
    thirdPartyData: JsObject,
    variables: Variables
)
