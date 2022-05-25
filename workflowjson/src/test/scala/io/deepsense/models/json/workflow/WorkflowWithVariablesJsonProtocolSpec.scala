package io.deepsense.models.json.workflow

import io.deepsense.models.json.graph.GraphJsonProtocol.GraphWriter
import io.deepsense.models.workflows._
import spray.json._

class WorkflowWithVariablesJsonProtocolSpec extends WorkflowJsonTestSupport
    with WorkflowWithVariablesJsonProtocol {

  "WorkflowWithVariables" should {

    "be serialized to json" in {
      val (workflow, json) = workflowWithVariablesFixture
      workflow.toJson shouldBe json
    }

    "be deserialized from json" in {
      val (workflow, json) = workflowWithVariablesFixture
      json.convertTo[WorkflowWithVariables] shouldBe workflow
    }
  }

  def workflowWithVariablesFixture: (WorkflowWithVariables, JsObject) = {

    val workflowId = Workflow.Id.randomId

    val workflow = WorkflowWithVariables(
      workflowId,
      WorkflowMetadata(WorkflowType.Batch, "0.4.0"),
      graph,
      JsObject("example" -> JsArray(JsNumber(1), JsNumber(2), JsNumber(3))),
      Variables())

    val workflowJson = JsObject(
      "id" -> JsString(workflowId.toString),
      "metadata" -> JsObject(
        "type" -> JsString("batch"),
        "apiVersion" -> JsString("0.4.0")
      ),
      "workflow" -> graph.toJson(GraphWriter),
      "thirdPartyData" -> JsObject(
        "example" -> JsArray(Vector(1, 2, 3).map(JsNumber(_)))
      ),
      "variables" -> JsObject()
    )

    (workflow, workflowJson)
  }

}
