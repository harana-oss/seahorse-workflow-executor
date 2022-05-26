package io.deepsense.workflowexecutor.communication.message.workflow

import spray.json.DefaultJsonProtocol
import spray.json.RootJsonFormat

import io.deepsense.commons.json.IdJsonProtocol
import io.deepsense.commons.utils.Logging
import io.deepsense.models.json.workflow.WorkflowJsonProtocol
import io.deepsense.models.workflows.Workflow

case class UpdateWorkflow(workflowId: Workflow.Id, workflow: Workflow)

trait UpdateWorkflowJsonProtocol
    extends DefaultJsonProtocol
    with IdJsonProtocol
    with WorkflowJsonProtocol
    with Logging {

  implicit val updateWorkflowFormat: RootJsonFormat[UpdateWorkflow] =
    jsonFormat2(UpdateWorkflow.apply)

}
