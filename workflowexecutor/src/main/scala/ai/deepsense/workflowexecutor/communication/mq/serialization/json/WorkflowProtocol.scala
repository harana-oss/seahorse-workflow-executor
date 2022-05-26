package ai.deepsense.workflowexecutor.communication.mq.serialization.json

import spray.json.JsObject

import ai.deepsense.models.json.graph.GraphJsonProtocol.GraphReader
import ai.deepsense.models.workflows.ExecutionReport
import ai.deepsense.models.workflows.InferredState
import ai.deepsense.models.workflows.WorkflowWithResults
import ai.deepsense.models.json.workflow.ExecutionReportJsonProtocol._
import ai.deepsense.models.json.workflow.InferredStateJsonProtocol
import ai.deepsense.models.json.workflow.WorkflowJsonProtocol
import ai.deepsense.models.json.workflow.WorkflowWithResultsJsonProtocol
import ai.deepsense.workflowexecutor.communication.message.workflow.AbortJsonProtocol._
import ai.deepsense.workflowexecutor.communication.message.workflow.SynchronizeJsonProtocol._
import ai.deepsense.workflowexecutor.communication.message.workflow._
import ai.deepsense.workflowexecutor.communication.mq.json.Constants.MessagesTypes._
import ai.deepsense.workflowexecutor.communication.mq.json.DefaultJsonMessageDeserializer
import ai.deepsense.workflowexecutor.communication.mq.json.DefaultJsonMessageSerializer
import ai.deepsense.workflowexecutor.communication.mq.json.JsonMessageDeserializer
import ai.deepsense.workflowexecutor.communication.mq.json.JsonMessageSerializer

object WorkflowProtocol {

  val abort          = "abort"

  val launch         = "launch"

  val updateWorkflow = "updateWorkflow"

  val synchronize    = "synchronize"

  object AbortDeserializer extends DefaultJsonMessageDeserializer[Abort](abort)

  object SynchronizeDeserializer extends DefaultJsonMessageDeserializer[Synchronize](synchronize)
  object SynchronizeSerializer   extends DefaultJsonMessageSerializer[Synchronize](synchronize)

  case class UpdateWorkflowDeserializer(graphReader: GraphReader)
      extends JsonMessageDeserializer
      with UpdateWorkflowJsonProtocol {

    private val defaultDeserializer =
      new DefaultJsonMessageDeserializer[UpdateWorkflow](updateWorkflow)

    override def deserialize: PartialFunction[(String, JsObject), Any] =
      defaultDeserializer.deserialize

  }

}
