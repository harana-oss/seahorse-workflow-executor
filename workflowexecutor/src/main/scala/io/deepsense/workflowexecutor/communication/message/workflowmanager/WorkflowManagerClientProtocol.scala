package io.deepsense.workflowexecutor.communication.message.workflowmanager

import io.deepsense.models.workflows.Workflow

object WorkflowManagerClientProtocol {

  case class GetWorkflow(workflowId: Workflow.Id)

}
