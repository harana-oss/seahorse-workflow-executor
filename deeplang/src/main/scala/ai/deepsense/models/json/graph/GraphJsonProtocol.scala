package ai.deepsense.models.json.graph

import spray.json._
import ai.deepsense.deeplang.Action
import ai.deepsense.deeplang.catalogs.actions.ActionCatalog
import ai.deepsense.deeplang.actions.CreateCustomTransformer
import ai.deepsense.deeplang.actions.UnknownOperation
import ai.deepsense.graph.FlowGraph.FlowNode
import ai.deepsense.graph.FlowGraph
import ai.deepsense.graph.Edge
import ai.deepsense.graph.Node
import ai.deepsense.models.json.graph.OperationJsonProtocol.ActionReader

object GraphJsonProtocol {

  import EdgeJsonProtocol._
  import NodeJsonProtocol._

  val Workflow = "workflow"

  val Nodes = "nodes"

  val Edges = "connections"

  val NodeId = "id"

  class GraphReader(val catalog: ActionCatalog) extends JsonReader[FlowGraph] with DefaultJsonProtocol {

    private val dOperationReader = new ActionReader(this)

    override def read(json: JsValue): FlowGraph = json match {
      case JsObject(fields) => read(fields)
      case x                =>
        throw new DeserializationException(s"Expected JsObject with a Graph but got $x")
    }

    private def readNode(nodeJs: JsValue): FlowNode = nodeJs match {
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
            nodeJs.convertTo[Action](dOperationReader)
          catch {
            case e: DeserializationException =>
              new UnknownOperation
          }
        Node(Node.Id.fromString(nodeId), operation)
      case x                =>
        throw new DeserializationException(s"Expected JsObject with a node but got $x")
    }

    private def readNodes(nodesJs: JsValue): Set[FlowNode] = nodesJs match {
      case JsArray(elements) => elements.map(readNode).toSet
      case x                 =>
        throw new DeserializationException(s"Expected JsArray with nodes but got $x")
    }

    private def readEdges(edgesJs: JsValue): Set[Edge] = edgesJs match {
      case JsArray(elements) => elements.map(_.convertTo[Edge]).toSet
      case x                 =>
        throw new DeserializationException(s"Expected JsArray with edges but got $x")
    }

    private def read(fields: Map[String, JsValue]): FlowGraph = {
      val nodes: Set[FlowNode] = fields.get(Nodes).map(readNodes).getOrElse(Set())
      val edges: Set[Edge]         = fields.get(Edges).map(readEdges).getOrElse(Set())
      FlowGraph(nodes, edges)
    }

  }

  implicit object GraphWriter extends JsonWriter[FlowGraph] with DefaultJsonProtocol {

    override def write(graph: FlowGraph): JsValue = {
      JsObject(
        Nodes -> JsArray(graph.nodes.map(_.toJson).toVector),
        Edges -> JsArray(graph.getValidEdges.map(_.toJson).toVector)
      )
    }

  }

}
