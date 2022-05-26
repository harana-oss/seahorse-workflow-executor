package ai.deepsense.workflowexecutor.communication.message.workflow

import spray.json.DefaultJsonProtocol
import spray.json.RootJsonFormat

import ai.deepsense.commons.json.IdJsonProtocol
import ai.deepsense.commons.utils.Logging
import ai.deepsense.models.json.workflow.WorkflowJsonProtocol
import ai.deepsense.models.workflows.Workflow

case class UpdateWorkflow(workflowId: Workflow.Id, workflow: Workflow)

trait UpdateWorkflowJsonProtocol
    extends DefaultJsonProtocol
    with IdJsonProtocol
    with WorkflowJsonProtocol
    with Logging {

  implicit val updateWorkflowFormat: RootJsonFormat[UpdateWorkflow] =
    jsonFormat2(UpdateWorkflow.apply)

}
