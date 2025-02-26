package ai.deepsense.models.json.graph

import spray.json._

import org.scalatest.matchers.should.Matchers
import ai.deepsense.graph.Edge
import ai.deepsense.graph.Endpoint
import ai.deepsense.graph.Node

class EdgeJsonProtocolSpec extends GraphJsonTestSupport {

  import ai.deepsense.models.json.graph.EdgeJsonProtocol._

  val expectedFromId: Node.Id = Node.Id.randomId

  val expectedFromPort = 1989

  val expectedToId: Node.Id = Node.Id.randomId

  val expectedToPort = 1337

  val edge = Edge(
    Endpoint(expectedFromId, expectedFromPort),
    Endpoint(expectedToId, expectedToPort)
  )

  "Edge transformed to Json" should {
    "have correct from and to" in {
      val edgeJson = edge.toJson.asJsObject
      assertEndpointMatchesJsObject(edge.from, edgeJson.fields("from").asJsObject)
      assertEndpointMatchesJsObject(edge.to, edgeJson.fields("to").asJsObject)
    }
  }

  "Edge transformed to Json and then read to Object" should {
    "be equal" in {
      edge.toJson.convertTo[Edge] shouldBe edge
    }
  }

}
