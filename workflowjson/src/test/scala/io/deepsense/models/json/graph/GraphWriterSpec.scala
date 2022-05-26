package io.deepsense.models.json.graph

import spray.json._

import io.deepsense.deeplang.DOperation
import io.deepsense.graph.Edge
import io.deepsense.graph.Endpoint
import io.deepsense.graph.Node
import io.deepsense.graph.DeeplangGraph

class GraphWriterSpec extends GraphJsonTestSupport {

  import io.deepsense.models.json.graph.GraphJsonProtocol.GraphWriter

  val operation1 = mockOperation(0, 1, DOperation.Id.randomId, "name1")

  val operation2 = mockOperation(1, 1, DOperation.Id.randomId, "name2")

  val operation3 = mockOperation(1, 1, DOperation.Id.randomId, "name3")

  val operation4 = mockOperation(2, 1, DOperation.Id.randomId, "name4")

  val node1 = Node(Node.Id.randomId, operation1)

  val node2 = Node(Node.Id.randomId, operation2)

  val node3 = Node(Node.Id.randomId, operation3)

  val node4 = Node(Node.Id.randomId, operation4)

  val nodes = Set(node1, node2, node3, node4)

  val edgesList = List((node1, node2, 0, 0), (node1, node3, 0, 0), (node2, node4, 0, 0), (node3, node4, 0, 1))

  val edges = edgesList.map(n => Edge(Endpoint(n._1.id, n._3), Endpoint(n._2.id, n._4))).toSet

  val graph = DeeplangGraph(nodes, edges)

  val graphJson = graph.toJson.asJsObject

  "Graph transformed to Json" should {
    "have 'nodes' field".which {
      "is a JsArray" in {
        assert(graphJson.fields.contains("nodes"))
        assert(graphJson.fields("nodes").isInstanceOf[JsArray])
      }
      "consists of all graph's nodes" in {
        val nodesArray      = graphJson.fields("nodes").asInstanceOf[JsArray]
        val expectedNodeIds = Set(node1, node2, node3, node4).map(_.id.value.toString)
        val actualNodeIds = nodesArray.elements
          .map(
            _.asJsObject
              .fields("id")
              .convertTo[String]
          )
          .toSet
        assert(actualNodeIds == expectedNodeIds)
      }
      "have values created by NodeFormat" in {
        val nodesArray = graphJson.fields("nodes").asInstanceOf[JsArray]
        val nodes      = Set(node1, node2, node3, node4)
        import io.deepsense.models.json.graph.NodeJsonProtocol._
        assert(nodes.forall { node =>
          val nodeJson = node.toJson
          nodesArray.elements.count(jsValue => jsValue.asJsObject == nodeJson) == 1
        })
      }
    }
    "have 'edges' field".which {
      "is a JsArray" in {
        assert(graphJson.fields.contains("connections"))
        assert(graphJson.fields("connections").isInstanceOf[JsArray])
      }
      "consists of all graph's connections" in {
        val edgesArray = graphJson.fields("connections").asInstanceOf[JsArray]
        assert(graph.edges.forall { edge =>
          edgesArray.elements.count {
            case edgeObject: JsObject =>
              endpointMatchesJsObject(edge.from, edgeObject.fields("from").asJsObject) &&
              endpointMatchesJsObject(edge.to, edgeObject.fields("to").asJsObject)
            case _ => false
          } == 1
        })
      }
    }
  }

}
