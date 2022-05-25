package io.deepsense.models.workflows

import io.deepsense.commons.exception.FailureDescription
import io.deepsense.graph.Node
import io.deepsense.graph.nodestate.NodeStatus

case class ExecutionReport(
    states: Map[Node.Id, NodeState],
    error: Option[FailureDescription] = None) {

  def nodesStatuses: Map[Node.Id, NodeStatus] = states.mapValues(_.nodeStatus)

  def resultEntities: EntitiesMap = {
    val combinedEntities = states.valuesIterator.flatMap(_.reportEntities().toSeq).toMap
    EntitiesMap(combinedEntities)
  }

  def statesOnly: ExecutionReport = copy(states = states.map(p => p._1 -> p._2.withoutReports))
}

object ExecutionReport {

  def apply(
      nodes: Map[Node.Id, NodeStatus],
      resultEntities: EntitiesMap,
      error: Option[FailureDescription]): ExecutionReport = {
    ExecutionReport(toNodeStates(nodes, resultEntities), error)
  }

  def statesOnly(
      nodes: Map[Node.Id, NodeStatus],
      error: Option[FailureDescription]): ExecutionReport = {
    ExecutionReport(nodes.mapValues(status => NodeState(status, None)), error)
  }

  private def toNodeStates(
      nodes: Map[Node.Id, NodeStatus],
      resultEntities: EntitiesMap): Map[Node.Id, NodeState] = {
    nodes.mapValues(status => NodeState(status, Some(resultEntities.subMap(status.results.toSet))))
  }
}
