package ai.deepsense.models.workflows

import ai.deepsense.commons.exception.FailureDescription
import ai.deepsense.commons.models.Entity
import ai.deepsense.graph.nodestate.Draft
import ai.deepsense.graph.nodestate.NodeStatus

/** @param nodeStatus
  *   Status of the node
  * @param reports
  *   None means we have no knowledge about reports. Empty EntitiesMap means there is no reports.
  */
case class NodeState(nodeStatus: NodeStatus, reports: Option[EntitiesMap]) {

  def reportEntities(): Map[Entity.Id, EntitiesMap.Entry] =
    reports.map(_.entities).getOrElse(Map())

  def withoutReports: NodeState = copy(reports = None)

  def abort: NodeState = copy(nodeStatus = nodeStatus.abort)

  def enqueue: NodeState = copy(nodeStatus = nodeStatus.enqueue)

  def draft: NodeState = copy(nodeStatus = Draft(nodeStatus.results))

  def fail(failureDescription: FailureDescription): NodeState =
    // failure means reseting reports
    copy(nodeStatus = nodeStatus.fail(failureDescription), Some(EntitiesMap()))

  def isCompleted: Boolean = nodeStatus.isCompleted

  def isDraft: Boolean = nodeStatus.isDraft

  def isQueued: Boolean = nodeStatus.isQueued

  def isRunning: Boolean = nodeStatus.isRunning

  def isFailed: Boolean = nodeStatus.isFailed

  def isAborted: Boolean = nodeStatus.isAborted

  def finish(entitiesIds: Seq[Entity.Id], results: EntitiesMap): NodeState =
    NodeState(nodeStatus.finish(entitiesIds), Some(results))

  def start: NodeState = copy(nodeStatus = nodeStatus.start)

}

object NodeState {

  def draft: NodeState =
    NodeState(Draft(), Some(EntitiesMap()))

}
