package ai.deepsense.workflowexecutor.communication.mq.serialization.json

import java.nio.charset.Charset

import org.scalatestplus.mockito.MockitoSugar
import spray.json._

import ai.deepsense.commons.StandardSpec
import ai.deepsense.commons.models.Entity
import ai.deepsense.deeplang.ActionObject
import ai.deepsense.deeplang.actionobjects.ColumnsFilterer
import ai.deepsense.graph._
import ai.deepsense.models.json.graph.GraphJsonProtocol.GraphReader
import ai.deepsense.models.json.workflow.ExecutionReportJsonProtocol
import ai.deepsense.models.json.workflow.InferredStateJsonProtocol
import ai.deepsense.models.json.workflow.WorkflowWithResultsJsonProtocol
import ai.deepsense.models.workflows._
import ai.deepsense.reportlib.model.factory.ReportContentTestFactory
import ai.deepsense.workflowexecutor.communication.message.global._
import ai.deepsense.workflowexecutor.communication.message.workflow.Synchronize

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

  private def expectedSerializationResult(messageType: String, jsonObject: JsValue): Array[Byte] = {
    JsObject(
      "messageType" -> JsString(messageType),
      "messageBody" -> jsonObject
    ).compactPrint.getBytes(Charset.forName("UTF-8"))
  }

}
