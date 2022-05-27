package ai.deepsense.models.json.graph

import org.scalatest.matchers.should.Matchers
import org.mockito.Mockito._
import spray.json._

import ai.deepsense.commons.json.IdJsonProtocol
import ai.deepsense.deeplang.Action
import ai.deepsense.graph.FlowGraph.FlowNode
import ai.deepsense.graph.Node

class NodeJsonProtocolSpec extends GraphJsonTestSupport with IdJsonProtocol {

  import ai.deepsense.models.json.graph.NodeJsonProtocol._

  "Node with Operation transformed to Json" should {
    val expectedOperationId   = Action.Id.randomId
    val expectedOperationName = "expectedName"
    val dOperation            = mock[Action]

    when(dOperation.id).thenReturn(expectedOperationId)
    when(dOperation.name).thenReturn(expectedOperationName)

    val node           = mock[FlowNode]
    val expectedNodeId = Node.Id.randomId
    when(node.value).thenReturn(dOperation)
    when(node.id).thenReturn(expectedNodeId)
    val nodeJson       = node.toJson.asJsObject

    "have correct 'id' field" in {
      nodeJson.fields("id").convertTo[String] shouldBe expectedNodeId.toString
    }

    "have correct 'operation' field" in {
      val operationField = nodeJson.fields("operation").asJsObject
      operationField.fields("id").convertTo[Action.Id] shouldBe expectedOperationId
      operationField.fields("name").convertTo[String] shouldBe expectedOperationName
    }
  }

}
