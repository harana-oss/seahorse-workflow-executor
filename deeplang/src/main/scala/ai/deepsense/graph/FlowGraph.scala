package ai.deepsense.graph

import java.util.UUID

import ai.deepsense.deeplang.Action
import ai.deepsense.graph.FlowGraph.FlowNode

case class FlowGraph(override val nodes: Set[FlowNode] = Set.empty, override val edges: Set[Edge] = Set())
    extends DirectedGraph[Action, FlowGraph](nodes, edges)
    with KnowledgeInference
    with NodeInferenceImpl {

  override def subgraph(nodes: Set[FlowNode], edges: Set[Edge]): FlowGraph =
    FlowGraph(nodes, edges)

  def getDatasourcesIds: Set[UUID] =
    nodes.foldLeft(Set.empty[UUID])((acc, el) => acc ++ el.value.getDatasourcesIds)

}

object FlowGraph {

  type FlowNode = Node[Action]

}
