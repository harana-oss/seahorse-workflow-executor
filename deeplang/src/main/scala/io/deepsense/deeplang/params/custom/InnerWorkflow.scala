package io.deepsense.deeplang.params.custom

import spray.json.JsObject

import io.deepsense.deeplang.DOperation
import io.deepsense.deeplang.doperations.custom.Sink
import io.deepsense.deeplang.doperations.custom.Source
import io.deepsense.graph.DeeplangGraph.DeeplangNode
import io.deepsense.graph.Node
import io.deepsense.graph.DeeplangGraph

case class InnerWorkflow(graph: DeeplangGraph, thirdPartyData: JsObject, publicParams: List[PublicParam] = List.empty) {

  require(findNodeOfType(Source.id).isDefined, "Inner workflow must have source node")

  require(findNodeOfType(Sink.id).isDefined, "Inner workflow must have sink node")

  val source: DeeplangNode = findNodeOfType(Source.id).get

  val sink: DeeplangNode = findNodeOfType(Sink.id).get

  private def findNodeOfType(operationId: DOperation.Id): Option[DeeplangNode] =
    graph.nodes.find(_.value.id == operationId)

}

object InnerWorkflow {

  val empty =
    InnerWorkflow(DeeplangGraph(Set(Node(Node.Id.randomId, Source()), Node(Node.Id.randomId, Sink()))), JsObject())

}

case class PublicParam(nodeId: Node.Id, paramName: String, publicName: String)
