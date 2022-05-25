package io.deepsense.workflowexecutor.communication.mq.json

import java.nio.charset.StandardCharsets

import org.scalatest.mockito.MockitoSugar
import spray.json._

import io.deepsense.commons.StandardSpec
import io.deepsense.commons.models.Entity
import io.deepsense.deeplang.DOperable
import io.deepsense.deeplang.doperables.ColumnsFilterer
import io.deepsense.graph.Node
import io.deepsense.models.json.workflow.ExecutionReportJsonProtocol._
import io.deepsense.models.workflows.{EntitiesMap, ExecutionReport, Workflow}
import io.deepsense.reportlib.model.factory.ReportContentTestFactory
import io.deepsense.workflowexecutor.communication.message.global._
import io.deepsense.workflowexecutor.communication.mq.json.Global.GlobalMQSerializer

class GlobalMQSerializerSpec
  extends StandardSpec
  with MockitoSugar {

    "GlobalMQSerializer" should {
      "serialize ExecutionReport" in {
        val executionReport = ExecutionReport(
          Map(Node.Id.randomId -> io.deepsense.graph.nodestate.Draft()),
          EntitiesMap(
            Map[Entity.Id, DOperable](
              Entity.Id.randomId -> new ColumnsFilterer),
            Map(Entity.Id.randomId -> ReportContentTestFactory.someReport)),
          None)

        serialize(executionReport) shouldBe asBytes(JsObject(
          "messageType" -> JsString("executionStatus"),
          "messageBody" -> executionReport.toJson))
      }

      "serialize Launch messages" in {
        val workflowId = Workflow.Id.randomId
        val nodesToExecute = Vector(Workflow.Id.randomId, Workflow.Id.randomId, Workflow.Id.randomId)
        val jsNodesToExecute = JsArray(nodesToExecute.map(id => JsString(id.toString)))

        val outMessage = JsObject(
          "messageType" -> JsString("launch"),
          "messageBody" -> JsObject(
            "workflowId" -> JsString(workflowId.toString),
            "nodesToExecute" -> jsNodesToExecute
          )
        )

        val serializedMessage = serialize(Launch(workflowId, nodesToExecute.toSet))
        serializedMessage shouldBe asBytes(outMessage)
      }

      "serialize Heartbeat messages" in {
        val workflowId = "foo-workflow"
        val outMessage = JsObject(
          "messageType" -> JsString("heartbeat"),
          "messageBody" -> JsObject(
            "workflowId" -> JsString(workflowId)))
        serialize(Heartbeat(workflowId)) shouldBe asBytes(outMessage)
      }
      "serialize PoisonPill messages" in {
        val outMessage = JsObject(
          "messageType" -> JsString("poisonPill"),
          "messageBody" -> JsObject())
        serialize(PoisonPill()) shouldBe asBytes(outMessage)
      }
      "serialize Ready messages" in {
        val sessionId = "foo-session"
        val outMessage = JsObject(
          "messageType" -> JsString("ready"),
          "messageBody" -> JsObject(
            "sessionId" -> JsString(sessionId)))
        serialize(Ready(sessionId)) shouldBe asBytes(outMessage)
      }
    }

    private def asBytes(jsObject: JsObject): Array[Byte] =
      jsObject.compactPrint.getBytes(StandardCharsets.UTF_8)

    private def serialize(message: Any): Array[Byte] =
      GlobalMQSerializer.serializeMessage(message)
}
