package io.deepsense.models.json.workflow

import io.deepsense.models.json.graph.NodeStatusJsonProtocol
import io.deepsense.models.workflows._

trait WorkflowWithResultsJsonProtocol
    extends WorkflowJsonProtocol
    with NodeStatusJsonProtocol
    with ExecutionReportJsonProtocol {

  implicit val workflowWithResultsFormat =
    jsonFormat(WorkflowWithResults, "id", "metadata", "workflow", "thirdPartyData", "executionReport", "workflowInfo")

}
