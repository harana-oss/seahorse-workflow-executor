package ai.deepsense.models.json.workflow

import spray.json._

import ai.deepsense.models.json.graph.GraphJsonProtocol.GraphWriter
import ai.deepsense.models.workflows.Workflow
import ai.deepsense.models.workflows.WorkflowMetadata
import ai.deepsense.models.workflows.WorkflowType

class WorkflowJsonProtocolSpec extends WorkflowJsonTestSupport with WorkflowJsonProtocol {

  "Workflow" should {
    "be serialized to json" in {
      val (workflow, json) = workflowFixture
      workflow.toJson shouldBe json
    }

    "be deserialized from json" in {
      val (workflow, json) = workflowFixture
      json.convertTo[Workflow] shouldBe workflow
    }
  }

  def workflowFixture: (Workflow, JsObject) = {
    val workflow     = Workflow(
      WorkflowMetadata(WorkflowType.Batch, "0.4.0"),
      graph,
      JsObject("example" -> JsArray(JsNumber(1), JsNumber(2), JsNumber(3)))
    )
    val workflowJson = JsObject(
      "metadata"       -> JsObject(
        "type"       -> JsString("batch"),
        "apiVersion" -> JsString("0.4.0")
      ),
      "workflow"       -> graph.toJson(GraphWriter),
      "thirdPartyData" -> JsObject(
        "example" -> JsArray(Vector(1, 2, 3).map(JsNumber(_)))
      )
    )
    (workflow, workflowJson)
  }

}
