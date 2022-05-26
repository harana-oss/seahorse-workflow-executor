package ai.deepsense.workflowexecutor.communication.message.workflowmanager

import ai.deepsense.models.workflows.Workflow

object WorkflowManagerClientProtocol {

  case class GetWorkflow(workflowId: Workflow.Id)

}
