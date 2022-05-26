package io.deepsense.workflowexecutor.communication.mq.serialization.json

import java.nio.charset.Charset

import org.scalatestplus.mockito.MockitoSugar
import spray.json._

import io.deepsense.commons.StandardSpec
import io.deepsense.commons.models.Entity
import io.deepsense.deeplang.DOperable
import io.deepsense.deeplang.doperables.ColumnsFilterer
import io.deepsense.graph._
import io.deepsense.models.json.graph.GraphJsonProtocol.GraphReader
import io.deepsense.models.json.workflow.ExecutionReportJsonProtocol
import io.deepsense.models.json.workflow.InferredStateJsonProtocol
import io.deepsense.models.json.workflow.WorkflowWithResultsJsonProtocol
import io.deepsense.models.workflows._
import io.deepsense.reportlib.model.factory.ReportContentTestFactory
import io.deepsense.workflowexecutor.communication.message.global._
import io.deepsense.workflowexecutor.communication.message.workflow.Synchronize

class ProtocolJsonSerializerSpec
    extends StandardSpec
    with MockitoSugar
    with WorkflowWithResultsJsonProtocol
    with InferredStateJsonProtocol
    with HeartbeatJsonProtocol {

  override val graphReader: GraphReader = mock[GraphReader]

  "ProtocolJsonSerializer" should {
    val protocolJsonSerializer = ProtocolJsonSerializer(graphReader)

    "serialize Synchronize messages" in {
      protocolJsonSerializer.serializeMessage(Synchronize()) shouldBe
        expectedSerializationResult("synchronize", JsObject())
    }

  }

  private def expectedSerializationResult(messageType: String, jsonObject: JsValue): Array[Byte] =
    JsObject(
      "messageType" -> JsString(messageType),
      "messageBody" -> jsonObject
    ).compactPrint.getBytes(Charset.forName("UTF-8"))

}
