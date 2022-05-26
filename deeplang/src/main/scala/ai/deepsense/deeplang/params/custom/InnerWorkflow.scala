package ai.deepsense.deeplang.params.custom

import java.util.UUID

import spray.json.JsObject

import ai.deepsense.deeplang.DOperation
import ai.deepsense.deeplang.doperations.custom.Sink
import ai.deepsense.deeplang.doperations.custom.Source
import ai.deepsense.graph.DeeplangGraph.DeeplangNode
import ai.deepsense.graph.DeeplangGraph
import ai.deepsense.graph.Node

case class InnerWorkflow(graph: DeeplangGraph, thirdPartyData: JsObject, publicParams: List[PublicParam] = List.empty) {

  require(findNodeOfType(Source.id).isDefined, "Inner workflow must have source node")
  require(findNodeOfType(Sink.id).isDefined, "Inner workflow must have sink node")

  val source: DeeplangNode = findNodeOfType(Source.id).get

  val sink: DeeplangNode = findNodeOfType(Sink.id).get

  private def findNodeOfType(operationId: DOperation.Id): Option[DeeplangNode] =
    graph.nodes.find(_.value.id == operationId)

  def getDatasourcesIds: Set[UUID] =
    graph.getDatasourcesIds

}

object InnerWorkflow {

  val empty =
    InnerWorkflow(DeeplangGraph(Set(Node(Node.Id.randomId, Source()), Node(Node.Id.randomId, Sink()))), JsObject())

}

case class PublicParam(nodeId: Node.Id, paramName: String, publicName: String)
