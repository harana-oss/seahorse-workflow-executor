package ai.deepsense.workflowexecutor.communication.mq.serialization.json

import java.nio.charset.StandardCharsets

import org.scalatestplus.mockito.MockitoSugar
import spray.json._

import ai.deepsense.commons.StandardSpec
import ai.deepsense.deeplang.CatalogRecorder
import ai.deepsense.graph.FlowGraph
import ai.deepsense.models.json.graph.GraphJsonProtocol.GraphReader
import ai.deepsense.models.workflows.Workflow
import ai.deepsense.models.workflows.WorkflowMetadata
import ai.deepsense.models.workflows.WorkflowType
import ai.deepsense.workflowexecutor.communication.message.workflow.Abort
import ai.deepsense.workflowexecutor.communication.message.workflow.Synchronize
import ai.deepsense.workflowexecutor.communication.message.workflow.UpdateWorkflow

class ProtocolJsonDeserializerSpec extends StandardSpec with MockitoSugar {

  "ProtocolJsonDeserializer" should {
    "deserialize Abort messages" in {
      val workflowId = Workflow.Id.randomId

      val rawMessage = JsObject(
        "messageType" -> JsString("abort"),
        "messageBody" -> JsObject(
          "workflowId" -> JsString(workflowId.toString)
        )
      )

      val readMessage: Any = serializeAndRead(rawMessage)
      readMessage shouldBe Abort(workflowId)
    }
    "deserialize UpdateWorkflow messages" in {
      val dOperationsCatalog   = CatalogRecorder.resourcesCatalogRecorder.catalogs.operations
      val graphReader          = new GraphReader(dOperationsCatalog)
      val protocolDeserializer = ProtocolJsonDeserializer(graphReader)
      val workflowId           = Workflow.Id.randomId

      val rawMessage = JsObject(
        "messageType" -> JsString("updateWorkflow"),
        "messageBody" -> JsObject(
          "workflowId" -> JsString(workflowId.toString),
          "workflow" -> JsObject(
            "metadata" -> JsObject(
              "type"       -> JsString("batch"),
              "apiVersion" -> JsString("1.0.0")
            ),
            "workflow" -> JsObject(
              "nodes"       -> JsArray(),
              "connections" -> JsArray()
            ),
            "thirdPartyData" -> JsObject()
          )
        )
      )

      val readMessage: Any = serializeAndRead(rawMessage, protocolDeserializer)
      readMessage shouldBe UpdateWorkflow(
        workflowId,
        Workflow(WorkflowMetadata(WorkflowType.Batch, "1.0.0"), FlowGraph(), JsObject())
      )
    }

    "deserialize Synchronize messages" in {
      val rawMessage = JsObject("messageType" -> JsString("synchronize"), "messageBody" -> JsObject())
      serializeAndRead(rawMessage) shouldBe Synchronize()
    }
  }

  private def serializeAndRead(
      rawMessage: JsObject,
      protocolDeserializer: ProtocolJsonDeserializer = ProtocolJsonDeserializer(mock[GraphReader])
  ): Any = {
    val bytes = rawMessage.compactPrint.getBytes(StandardCharsets.UTF_8)
    protocolDeserializer.deserializeMessage(bytes)
  }

}
