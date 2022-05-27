package ai.deepsense.models.workflows

import spray.json.JsObject

import ai.deepsense.graph.FlowGraph

case class WorkflowWithResults(
                                id: Workflow.Id,
                                metadata: WorkflowMetadata,
                                graph: FlowGraph,
                                thirdPartyData: JsObject,
                                executionReport: ExecutionReport,
                                workflowInfo: WorkflowInfo
)
