package io.deepsense.workflowexecutor.communication.mq.json

import java.nio.charset.StandardCharsets

import org.scalatest.mockito.MockitoSugar
import spray.json.{JsArray, JsObject, JsString}

import io.deepsense.commons.StandardSpec
import io.deepsense.models.workflows.Workflow
import io.deepsense.workflowexecutor.communication.message.global._
import io.deepsense.workflowexecutor.communication.mq.json.Global.GlobalMQDeserializer

class GlobalMQDeserializerSpec
  extends StandardSpec
  with MockitoSugar {

  "GlobalMQDeserializer" should {
    "deserialize Launch messages" in {
      val workflowId = Workflow.Id.randomId
      val nodesToExecute = Vector(Workflow.Id.randomId, Workflow.Id.randomId, Workflow.Id.randomId)
      val jsNodesToExecute = JsArray(nodesToExecute.map(id => JsString(id.toString)))

      val rawMessage = JsObject(
        "messageType" -> JsString("launch"),
        "messageBody" -> JsObject(
          "workflowId" -> JsString(workflowId.toString),
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
        "messageBody" -> JsObject(
          "workflowId" -> JsString(workflowId)))
      serializeAndRead(rawMessage) shouldBe Heartbeat(workflowId)
    }
    "deserialize PoisonPill messages" in {
      val rawMessage = JsObject(
        "messageType" -> JsString("poisonPill"),
        "messageBody" -> JsObject())
      serializeAndRead(rawMessage) shouldBe PoisonPill()
    }
    "deserialize Ready messages" in {
      val sessionId = "foo-session"
      val rawMessage = JsObject(
        "messageType" -> JsString("ready"),
        "messageBody" -> JsObject(
          "sessionId" -> JsString(sessionId)))
      serializeAndRead(rawMessage) shouldBe Ready(sessionId)
    }
  }

  private def serializeAndRead(
    rawMessage: JsObject): Any = {
    val bytes = rawMessage.compactPrint.getBytes(StandardCharsets.UTF_8)
    GlobalMQDeserializer.deserializeMessage(bytes)
  }
}
