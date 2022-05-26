package ai.deepsense.models.json.workflow

import ai.deepsense.models.json.graph.NodeStatusJsonProtocol
import ai.deepsense.models.workflows.WorkflowWithResults

trait WorkflowWithResultsJsonProtocol
    extends WorkflowJsonProtocol
    with NodeStatusJsonProtocol
    with ExecutionReportJsonProtocol {

  implicit val workflowWithResultsFormat =
    jsonFormat(WorkflowWithResults, "id", "metadata", "workflow", "thirdPartyData", "executionReport", "workflowInfo")

}
