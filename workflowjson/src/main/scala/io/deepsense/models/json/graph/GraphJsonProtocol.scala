package io.deepsense.models.json.graph

import spray.json._
import io.deepsense.deeplang.DOperation
import io.deepsense.deeplang.catalogs.doperations.DOperationsCatalog
import io.deepsense.deeplang.doperations.CreateCustomTransformer
import io.deepsense.deeplang.doperations.UnknownOperation
import io.deepsense.graph.DeeplangGraph.DeeplangNode
import io.deepsense.graph.DeeplangGraph
import io.deepsense.graph.Edge
import io.deepsense.graph.Node
import io.deepsense.models.json.graph.OperationJsonProtocol.DOperationReader

object GraphJsonProtocol {

  import EdgeJsonProtocol._
  import NodeJsonProtocol._

  val Workflow = "workflow"

  val Nodes = "nodes"

  val Edges = "connections"

  val NodeId = "id"

  class GraphReader(catalog: DOperationsCatalog) extends JsonReader[DeeplangGraph] with DefaultJsonProtocol {

    private val dOperationReader = new DOperationReader(catalog)

    override def read(json: JsValue): DeeplangGraph = json match {
      case JsObject(fields) => read(fields)
      case x =>
        throw new DeserializationException(s"Expected JsObject with a Graph but got $x")
    }

    private def readNode(nodeJs: JsValue): DeeplangNode = nodeJs match {
      case JsObject(fields) =>
        val nodeId =
          try
            fields(NodeId).convertTo[String]
          catch {
            case e: Throwable =>
              throw new DeserializationException(s"Node is missing a string field '$NodeId'", e)
          }
        val operation =
          try {
            val operation = nodeJs.convertTo[DOperation](dOperationReader)
            operation match {
              case customTransformer: CreateCustomTransformer =>
                // convert workflow JSON to graph and then back to JSON to handle unknown operations
                val JsObject(fields) = customTransformer.getInnerWorkflow
                val reparsedGraph    = read(fields(Workflow)).toJson(GraphWriter)
                customTransformer.setInnerWorkflow(JsObject(fields.updated(Workflow, reparsedGraph)))
              case _ => operation
            }
          } catch {
            case e: DeserializationException =>
              new UnknownOperation
          }
        Node(Node.Id.fromString(nodeId), operation)
      case x =>
        throw new DeserializationException(s"Expected JsObject with a node but got $x")
    }

    private def readNodes(nodesJs: JsValue): Set[DeeplangNode] = nodesJs match {
      case JsArray(elements) => elements.map(readNode).toSet
      case x =>
        throw new DeserializationException(s"Expected JsArray with nodes but got $x")
    }

    private def readEdges(edgesJs: JsValue): Set[Edge] = edgesJs match {
      case JsArray(elements) => elements.map(_.convertTo[Edge]).toSet
      case x =>
        throw new DeserializationException(s"Expected JsArray with edges but got $x")
    }

    private def read(fields: Map[String, JsValue]): DeeplangGraph = {
      val nodes: Set[DeeplangNode] = fields.get(Nodes).map(readNodes).getOrElse(Set())
      val edges: Set[Edge]         = fields.get(Edges).map(readEdges).getOrElse(Set())
      DeeplangGraph(nodes, edges)
    }

  }

  implicit object GraphWriter extends JsonWriter[DeeplangGraph] with DefaultJsonProtocol {

    override def write(graph: DeeplangGraph): JsValue =
      JsObject(
        Nodes -> JsArray(graph.nodes.map(_.toJson).toVector),
        Edges -> JsArray(graph.getValidEdges.map(_.toJson).toVector)
      )

  }

}
