package io.deepsense.graph

trait TopologicallySortable[T] {
  def topologicallySorted: Option[List[Node[T]]]
  def allPredecessorsOf(id: Node.Id): Set[Node[T]]
  def edges: Set[Edge]
  def nodes: Set[Node[T]]
  def node(id: Node.Id): Node[T]
  def predecessors(id: Node.Id): IndexedSeq[Option[Endpoint]]
  def successors(id: Node.Id): IndexedSeq[Set[Endpoint]]
}
