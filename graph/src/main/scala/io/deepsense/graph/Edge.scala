package io.deepsense.graph

case class Endpoint(nodeId: Node.Id, portIndex: Int)

case class Edge(from: Endpoint, to: Endpoint)

object Edge {

  def apply(node1: Node[_], portIndex1: Int, node2: Node[_], portIndex2: Int): Edge =
    Edge(Endpoint(node1.id, portIndex1), Endpoint(node2.id, portIndex2))

}
