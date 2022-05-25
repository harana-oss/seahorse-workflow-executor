package io.deepsense.workflowexecutor.communication.mq.serialization.json

import spray.json.JsObject

import io.deepsense.models.json.graph.GraphJsonProtocol.GraphReader
import io.deepsense.models.workflows.{ExecutionReport, InferredState, WorkflowWithResults}
import io.deepsense.models.json.workflow.ExecutionReportJsonProtocol._
import io.deepsense.models.json.workflow.{InferredStateJsonProtocol, WorkflowJsonProtocol, WorkflowWithResultsJsonProtocol}
import io.deepsense.workflowexecutor.communication.message.workflow.AbortJsonProtocol._
import io.deepsense.workflowexecutor.communication.message.workflow.SynchronizeJsonProtocol._
import io.deepsense.workflowexecutor.communication.message.workflow._
import io.deepsense.workflowexecutor.communication.mq.json.Constants.MessagesTypes._
import io.deepsense.workflowexecutor.communication.mq.json.{DefaultJsonMessageDeserializer, DefaultJsonMessageSerializer, JsonMessageDeserializer, JsonMessageSerializer}

object WorkflowProtocol {
  val abort = "abort"
  val launch = "launch"
  val updateWorkflow = "updateWorkflow"
  val synchronize = "synchronize"

  object AbortDeserializer extends DefaultJsonMessageDeserializer[Abort](abort)

  object SynchronizeDeserializer extends DefaultJsonMessageDeserializer[Synchronize](synchronize)
  object SynchronizeSerializer extends DefaultJsonMessageSerializer[Synchronize](synchronize)

  case class UpdateWorkflowDeserializer(graphReader: GraphReader)
    extends JsonMessageDeserializer
    with UpdateWorkflowJsonProtocol {

    private val defaultDeserializer =
      new DefaultJsonMessageDeserializer[UpdateWorkflow](updateWorkflow)

    override def deserialize: PartialFunction[(String, JsObject), Any] =
      defaultDeserializer.deserialize
  }
}
