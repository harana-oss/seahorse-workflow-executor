package ai.deepsense.models.json.graph

import spray.json._
import ai.deepsense.deeplang.DOperation
import ai.deepsense.deeplang.catalogs.doperations.DOperationsCatalog
import ai.deepsense.deeplang.doperations.CreateCustomTransformer
import ai.deepsense.deeplang.doperations.UnknownOperation
import ai.deepsense.graph.DeeplangGraph.DeeplangNode
import ai.deepsense.graph.DeeplangGraph
import ai.deepsense.graph.Edge
import ai.deepsense.graph.Node
import ai.deepsense.models.json.graph.OperationJsonProtocol.DOperationReader

object GraphJsonProtocol {

  import EdgeJsonProtocol._
  import NodeJsonProtocol._

  val Workflow = "workflow"

  val Nodes = "nodes"

  val Edges = "connections"

  val NodeId = "id"

  class GraphReader(val catalog: DOperationsCatalog) extends JsonReader[DeeplangGraph] with DefaultJsonProtocol {

    private val dOperationReader = new DOperationReader(this)

    override def read(json: JsValue): DeeplangGraph = json match {
      case JsObject(fields) => read(fields)
      case x                =>
        throw new DeserializationException(s"Expected JsObject with a Graph but got $x")
    }

    private def readNode(nodeJs: JsValue): DeeplangNode = nodeJs match {
      case JsObject(fields) =>
        val nodeId    =
          try
            fields(NodeId).convertTo[String]
          catch {
            case e: Throwable =>
              throw new DeserializationException(s"Node is missing a string field '$NodeId'", e)
          }
        val operation =
          try
            nodeJs.convertTo[DOperation](dOperationReader)
          catch {
            case e: DeserializationException =>
              new UnknownOperation
          }
        Node(Node.Id.fromString(nodeId), operation)
      case x                =>
        throw new DeserializationException(s"Expected JsObject with a node but got $x")
    }

    private def readNodes(nodesJs: JsValue): Set[DeeplangNode] = nodesJs match {
      case JsArray(elements) => elements.map(readNode).toSet
      case x                 =>
        throw new DeserializationException(s"Expected JsArray with nodes but got $x")
    }

    private def readEdges(edgesJs: JsValue): Set[Edge] = edgesJs match {
      case JsArray(elements) => elements.map(_.convertTo[Edge]).toSet
      case x                 =>
        throw new DeserializationException(s"Expected JsArray with edges but got $x")
    }

    private def read(fields: Map[String, JsValue]): DeeplangGraph = {
      val nodes: Set[DeeplangNode] = fields.get(Nodes).map(readNodes).getOrElse(Set())
      val edges: Set[Edge]         = fields.get(Edges).map(readEdges).getOrElse(Set())
      DeeplangGraph(nodes, edges)
    }

  }

  implicit object GraphWriter extends JsonWriter[DeeplangGraph] with DefaultJsonProtocol {

    override def write(graph: DeeplangGraph): JsValue = {
      JsObject(
        Nodes -> JsArray(graph.nodes.map(_.toJson).toVector),
        Edges -> JsArray(graph.getValidEdges.map(_.toJson).toVector)
      )
    }

  }

}
