package io.deepsense.graph

import scala.collection.mutable

private[graph] class TopologicalSort[T](sortable: TopologicallySortable[T]) {
  import TopologicalSort._
  private val visits: mutable.Map[Node[T], Visits] = mutable.Map()

  private var sorted: Option[List[Node[T]]] = Some(List.empty)
  sortable.nodes.foreach(n => sorted = topologicalSort(n, sorted))

  def isSorted: Boolean = sorted.isEmpty
  def sortedNodes: Option[List[Node[T]]] = sorted

  private def topologicalSort(
    node: Node[T],
    sortedSoFar: Option[List[Node[T]]]): Option[List[Node[T]]] = {

    visits.get(node) match {
      case Some(Visited) => sortedSoFar
      case Some(InProgress) => None // we are revisiting a node; it is a cycle!
      case None => // node not yet visited
        markInProgress(node)
        var l = sortedSoFar
        sortable
          .successors(node.id)
          .foreach(_.map(_.nodeId).foreach(s => l = topologicalSort(sortable.node(s), l)))
        markVisited(node)
        l.map(node::_)
    }
  }

  private def markInProgress(node: Node[T]): Unit = {
    visits(node) = InProgress
  }

  private def markVisited(node: Node[T]): Unit = {
    visits(node) = Visited
  }
}

private[graph] object TopologicalSort {
  private sealed abstract class Visits
  private case object InProgress extends Visits
  private case object Visited extends Visits
}


