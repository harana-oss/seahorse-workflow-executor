package ai.deepsense.deeplang.parameters.custom

import java.util.UUID

import spray.json.JsObject

import ai.deepsense.deeplang.Action
import ai.deepsense.deeplang.actions.custom.Sink
import ai.deepsense.deeplang.actions.custom.Source
import ai.deepsense.graph.FlowGraph.FlowNode
import ai.deepsense.graph.FlowGraph
import ai.deepsense.graph.Node

case class InnerWorkflow(graph: FlowGraph, thirdPartyData: JsObject, publicParams: List[PublicParam] = List.empty) {

  require(findNodeOfType(Source.id).isDefined, "Inner workflow must have source node")
  require(findNodeOfType(Sink.id).isDefined, "Inner workflow must have sink node")

  val source: FlowNode = findNodeOfType(Source.id).get

  val sink: FlowNode = findNodeOfType(Sink.id).get

  private def findNodeOfType(operationId: Action.Id): Option[FlowNode] =
    graph.nodes.find(_.value.id == operationId)

  def getDatasourcesIds: Set[UUID] =
    graph.getDatasourcesIds

}

object InnerWorkflow {

  val empty =
    InnerWorkflow(FlowGraph(Set(Node(Node.Id.randomId, Source()), Node(Node.Id.randomId, Sink()))), JsObject())

}

case class PublicParam(nodeId: Node.Id, paramName: String, publicName: String)
