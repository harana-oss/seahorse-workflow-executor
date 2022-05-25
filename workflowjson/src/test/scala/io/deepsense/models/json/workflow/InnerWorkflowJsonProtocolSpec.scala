package io.deepsense.models.json.workflow

import spray.json._

import io.deepsense.deeplang.params.custom.{PublicParam, InnerWorkflow}
import io.deepsense.graph.Node
import io.deepsense.models.json.graph.GraphJsonProtocol.{GraphReader, GraphWriter}

class InnerWorkflowJsonProtocolSpec extends WorkflowTestSupport with InnerWorkflowJsonProtocol {

  override val graphReader: GraphReader = new GraphReader(catalog)

  val nodeId = Node.Id.randomId

  "InnerWorkflow" should {
    "be serialized to json" in {
      val (innerWorkflow, json) = innerWorkflowFixture
      innerWorkflow.toJson shouldBe json
    }

    "be deserialized from json" in {
      val (innerWorkflow, json) = innerWorkflowFixture
      json.convertTo[InnerWorkflow] shouldBe innerWorkflow
    }
  }

  def innerWorkflowFixture: (InnerWorkflow, JsObject) = {
    val innerWorkflow = InnerWorkflow(
      innerWorkflowGraph,
      JsObject(
        "example" -> JsArray(JsNumber(1), JsNumber(2), JsNumber(3))
      ),
      List(PublicParam(nodeId, "name", "public")))
    val innerWorkflowJson = JsObject(
      "workflow" -> innerWorkflowGraph.toJson(GraphWriter),
      "thirdPartyData" -> JsObject(
        "example" -> JsArray(Vector(1, 2, 3).map(JsNumber(_)))
      ),
      "publicParams" -> JsArray(
        JsObject(
          "nodeId" -> JsString(nodeId.toString),
          "paramName" -> JsString("name"),
          "publicName" -> JsString("public")
        )
      )
    )
    (innerWorkflow, innerWorkflowJson)
  }
}
