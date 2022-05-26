package io.deepsense.models.workflows

import spray.json.JsObject

import io.deepsense.graph.DeeplangGraph

case class WorkflowWithResults(
    id: Workflow.Id,
    metadata: WorkflowMetadata,
    graph: DeeplangGraph,
    thirdPartyData: JsObject,
    executionReport: ExecutionReport,
    workflowInfo: WorkflowInfo
)
