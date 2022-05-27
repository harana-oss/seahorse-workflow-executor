package ai.deepsense.graph

import java.util.UUID

import ai.deepsense.deeplang.Action
import ai.deepsense.graph.DeeplangGraph.DeeplangNode

case class DeeplangGraph(override val nodes: Set[DeeplangNode] = Set.empty, override val edges: Set[Edge] = Set())
    extends DirectedGraph[Action, DeeplangGraph](nodes, edges)
    with KnowledgeInference
    with NodeInferenceImpl {

  override def subgraph(nodes: Set[DeeplangNode], edges: Set[Edge]): DeeplangGraph =
    DeeplangGraph(nodes, edges)

  def getDatasourcesIds: Set[UUID] =
    nodes.foldLeft(Set.empty[UUID])((acc, el) => acc ++ el.value.getDatasourcesIds)

}

object DeeplangGraph {

  type DeeplangNode = Node[Action]

}
