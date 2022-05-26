package io.deepsense.models.json.graph

import org.mockito.Mockito._
import spray.json._

import io.deepsense.commons.json.IdJsonProtocol
import io.deepsense.deeplang.DOperation
import io.deepsense.graph.DeeplangGraph.DeeplangNode
import io.deepsense.graph.Node

class NodeJsonProtocolSpec extends GraphJsonTestSupport with IdJsonProtocol {

  import io.deepsense.models.json.graph.NodeJsonProtocol._

  "Node with Operation transformed to Json" should {
    val expectedOperationId   = DOperation.Id.randomId
    val expectedOperationName = "expectedName"
    val dOperation            = mock[DOperation]

    when(dOperation.id).thenReturn(expectedOperationId)
    when(dOperation.name).thenReturn(expectedOperationName)

    val node           = mock[DeeplangNode]
    val expectedNodeId = Node.Id.randomId
    when(node.value).thenReturn(dOperation)
    when(node.id).thenReturn(expectedNodeId)
    val nodeJson = node.toJson.asJsObject

    "have correct 'id' field" in {
      nodeJson.fields("id").convertTo[String] shouldBe expectedNodeId.toString
    }

    "have correct 'operation' field" in {
      val operationField = nodeJson.fields("operation").asJsObject
      operationField.fields("id").convertTo[DOperation.Id] shouldBe expectedOperationId
      operationField.fields("name").convertTo[String] shouldBe expectedOperationName
    }
  }

}
