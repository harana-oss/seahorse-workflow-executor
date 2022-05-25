package io.deepsense.models.json.workflow

import io.deepsense.models.workflows.{Variables, WorkflowWithVariables}

trait WorkflowWithVariablesJsonProtocol extends WorkflowJsonProtocol {

  implicit val variablesFormat = jsonFormat0(Variables)

  implicit val workflowWithVariablesFormat =
    jsonFormat(WorkflowWithVariables, "id", "metadata", "workflow", "thirdPartyData", "variables")
}
