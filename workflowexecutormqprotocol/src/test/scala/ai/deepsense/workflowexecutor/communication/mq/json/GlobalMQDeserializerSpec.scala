package ai.deepsense.workflowexecutor.communication.mq.json

import java.nio.charset.StandardCharsets

import org.scalatestplus.mockito.MockitoSugar
import spray.json.JsArray
import spray.json.JsNull
import spray.json.JsObject
import spray.json.JsString
import ai.deepsense.commons.StandardSpec
import ai.deepsense.models.workflows.Workflow
import ai.deepsense.workflowexecutor.communication.message.global._
import ai.deepsense.workflowexecutor.communication.mq.json.Global.GlobalMQDeserializer

class GlobalMQDeserializerSpec extends StandardSpec with MockitoSugar {

  "GlobalMQDeserializer" should {
    "deserialize Launch messages" in {
      val workflowId       = Workflow.Id.randomId
      val nodesToExecute   = Vector(Workflow.Id.randomId, Workflow.Id.randomId, Workflow.Id.randomId)
      val jsNodesToExecute = JsArray(nodesToExecute.map(id => JsString(id.toString)))

      val rawMessage = JsObject(
        "messageType" -> JsString("launch"),
        "messageBody" -> JsObject(
          "workflowId"     -> JsString(workflowId.toString),
          "nodesToExecute" -> jsNodesToExecute
        )
      )

      val readMessage: Any = serializeAndRead(rawMessage)
      readMessage shouldBe Launch(workflowId, nodesToExecute.toSet)
    }

    "deserialize Heartbeat messages" in {
      val workflowId = "foo-workflow"
      val rawMessage = JsObject(
        "messageType" -> JsString("heartbeat"),
        "messageBody" -> JsObject("workflowId" -> JsString(workflowId), "sparkUiAddress" -> JsNull)
      )
      serializeAndRead(rawMessage) shouldBe Heartbeat(workflowId, None)
    }
    "deserialize PoisonPill messages" in {
      val rawMessage = JsObject("messageType" -> JsString("poisonPill"), "messageBody" -> JsObject())
      serializeAndRead(rawMessage) shouldBe PoisonPill()
    }
    "deserialize Ready messages" in {
      val sessionId  = "foo-session"
      val rawMessage =
        JsObject("messageType" -> JsString("ready"), "messageBody" -> JsObject("sessionId" -> JsString(sessionId)))
      serializeAndRead(rawMessage) shouldBe Ready(sessionId)
    }
  }

  private def serializeAndRead(rawMessage: JsObject): Any = {
    val bytes = rawMessage.compactPrint.getBytes(StandardCharsets.UTF_8)
    GlobalMQDeserializer.deserializeMessage(bytes)
  }

}
