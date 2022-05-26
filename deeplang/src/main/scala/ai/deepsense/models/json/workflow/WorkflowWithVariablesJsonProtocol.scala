package ai.deepsense.models.json.workflow

import ai.deepsense.models.workflows.Variables
import ai.deepsense.models.workflows.WorkflowWithVariables

trait WorkflowWithVariablesJsonProtocol extends WorkflowJsonProtocol {

  implicit val variablesFormat = jsonFormat0(Variables)

  implicit val workflowWithVariablesFormat =
    jsonFormat(WorkflowWithVariables, "id", "metadata", "workflow", "thirdPartyData", "variables")

}
