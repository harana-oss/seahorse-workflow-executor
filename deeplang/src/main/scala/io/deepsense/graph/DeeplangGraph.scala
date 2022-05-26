package io.deepsense.graph

import io.deepsense.deeplang.DOperation
import io.deepsense.graph.DeeplangGraph.DeeplangNode

case class DeeplangGraph(override val nodes: Set[DeeplangNode] = Set.empty, override val edges: Set[Edge] = Set())
    extends DirectedGraph[DOperation, DeeplangGraph](nodes, edges)
    with KnowledgeInference
    with NodeInferenceImpl {

  override def subgraph(nodes: Set[DeeplangNode], edges: Set[Edge]): DeeplangGraph =
    DeeplangGraph(nodes, edges)

}

object DeeplangGraph {

  type DeeplangNode = Node[DOperation]

}
