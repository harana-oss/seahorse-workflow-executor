package io.deepsense.workflowexecutor.communication.mq.json

import java.nio.charset.Charset

import io.deepsense.deeplang.CatalogRecorder
import io.deepsense.models.json.graph.GraphJsonProtocol.GraphReader
import io.deepsense.models.json.workflow.InferredStateJsonProtocol
import io.deepsense.models.json.workflow.InferredStateJsonProtocol._
import io.deepsense.models.json.workflow.ExecutionReportJsonProtocol._
import io.deepsense.models.workflows.{ExecutionReport, InferredState}
import io.deepsense.workflowexecutor.communication.message.global._
import io.deepsense.workflowexecutor.communication.message.global.HeartbeatJsonProtocol._
import io.deepsense.workflowexecutor.communication.message.global.PoisonPillJsonProtocol._
import io.deepsense.workflowexecutor.communication.message.global.ReadyJsonProtocol._
import io.deepsense.workflowexecutor.communication.message.global.LaunchJsonProtocol._

object Global {
  val charset = Charset.forName("UTF-8")

  val dOperationsCatalog = CatalogRecorder.resourcesCatalogRecorder.catalogs.dOperationsCatalog

  val graphReader = new GraphReader(dOperationsCatalog)

  val inferredStateJsonProtocol = InferredStateJsonProtocol(graphReader)
  import inferredStateJsonProtocol._

  import Constants.MessagesTypes._

  object HeartbeatDeserializer extends DefaultJsonMessageDeserializer[Heartbeat](heartbeat)
  object HeartbeatSerializer extends DefaultJsonMessageSerializer[Heartbeat](heartbeat)

  object PoisonPillDeserializer extends DefaultJsonMessageDeserializer[PoisonPill](poisonPill)
  object PoisonPillSerializer extends DefaultJsonMessageSerializer[PoisonPill](poisonPill)

  object ReadyDeserializer extends DefaultJsonMessageDeserializer[Ready](ready)
  object ReadySerializer extends DefaultJsonMessageSerializer[Ready](ready)

  object LaunchDeserializer extends DefaultJsonMessageDeserializer[Launch](launch)
  object LaunchSerializer extends DefaultJsonMessageSerializer[Launch](launch)

  object ExecutionReportSerializer extends DefaultJsonMessageSerializer[ExecutionReport](executionReport)
  object ExecutionReportDeserializer extends DefaultJsonMessageDeserializer[ExecutionReport](executionReport)

  object InferredStateSerializer extends DefaultJsonMessageSerializer[InferredState](inferredState)
  object InferredStateDeserializer extends DefaultJsonMessageDeserializer[InferredState](inferredState)

  object GlobalMQSerializer extends JsonMQSerializer(
    Seq(HeartbeatSerializer,
      PoisonPillSerializer,
      ReadySerializer,
      LaunchSerializer,
      ExecutionReportSerializer,
      InferredStateSerializer
    ))

  object GlobalMQDeserializer extends JsonMQDeserializer(
    Seq(HeartbeatDeserializer,
      PoisonPillDeserializer,
      ReadyDeserializer,
      LaunchDeserializer,
      ExecutionReportDeserializer,
      InferredStateDeserializer
    ))
}
