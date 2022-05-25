package io.deepsense.workflowexecutor

import org.scalatest.BeforeAndAfter
import org.scalatest.mockito.MockitoSugar
import spray.json._

import io.deepsense.commons.StandardSpec

class WorkflowJsonParamsOverriderSpec
    extends StandardSpec
    with BeforeAndAfter
    with MockitoSugar
    with DefaultJsonProtocol {

  "WorkflowJsonParamsOverrider" should {
    "override parameters based on passed extra params" in {
      val overrides = Map(
        "node1.param with spaces" -> "new value",
        "node2.nested.parameter.test" -> "changed"
      )

      WorkflowJsonParamsOverrider.overrideParams(originalJson, overrides) shouldBe expectedJson
    }

    "throw when invalid parameters are passed" in {
      val overrides = Map(
        "node1.no such param" -> "no such param",
        "no such node.param" -> "no such node"
      )

      a[RuntimeException] should be thrownBy {
        WorkflowJsonParamsOverrider.overrideParams(originalJson, overrides)
      }
    }
  }

  val originalJson =
    """{
      |  "workflow": {
      |    "nodes": [{
      |      "id": "node1",
      |      "parameters": {
      |        "param with spaces": "value"
      |      }
      |     }, {
      |      "id": "node2",
      |      "parameters": {
      |        "param": "value",
      |        "nested": {
      |          "parameter": {
      |            "test": "nested value"
      |          }
      |        }
      |      }
      |    }]
      |  }
      |}
    """.stripMargin.parseJson

  val expectedJson =
    """{
      |  "workflow": {
      |    "nodes": [{
      |      "id": "node1",
      |      "parameters": {
      |        "param with spaces": "new value"
      |      }
      |     }, {
      |      "id": "node2",
      |      "parameters": {
      |        "param": "value",
      |        "nested": {
      |          "parameter": {
      |            "test": "changed"
      |          }
      |        }
      |      }
      |    }]
      |  }
      |}
    """.stripMargin.parseJson
}
