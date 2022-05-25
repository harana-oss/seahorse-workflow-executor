package io.deepsense.models.workflows

import io.deepsense.commons.exception.FailureDescription
import io.deepsense.commons.models.Entity
import io.deepsense.deeplang.inference.InferenceWarnings
import io.deepsense.deeplang.{DKnowledge, DOperable}
import io.deepsense.graph.NodeInferenceResult
import io.deepsense.reportlib.model.ReportContent

case class NodeStateWithResults(
    nodeState: NodeState,
    dOperables: Map[Entity.Id, DOperable],
    knowledge: Option[NodeInferenceResult]) {

  def abort: NodeStateWithResults = copy(nodeState = nodeState.abort)
  def enqueue: NodeStateWithResults = copy(nodeState = nodeState.enqueue)
  def draft: NodeStateWithResults = copy(nodeState = nodeState.draft)
  def fail(failureDescription: FailureDescription): NodeStateWithResults = {
    copy(nodeState = nodeState.fail(failureDescription))
  }
  def withKnowledge(inferredKnowledge: NodeInferenceResult): NodeStateWithResults = {
    copy(knowledge = Some(inferredKnowledge))
  }
  def clearKnowledge: NodeStateWithResults = copy(knowledge = None)
  def isCompleted: Boolean = nodeState.isCompleted
  def isQueued: Boolean = nodeState.isQueued
  def isRunning: Boolean = nodeState.isRunning
  def isFailed: Boolean = nodeState.isFailed
  def isAborted: Boolean = nodeState.isAborted
  def isDraft: Boolean = nodeState.isDraft
  def start: NodeStateWithResults = copy(nodeState = nodeState.start)
  def finish(
      entitiesIds: Seq[Entity.Id],
      reports: Map[Entity.Id, ReportContent],
      dOperables: Map[Entity.Id, DOperable]): NodeStateWithResults = {
    val results = EntitiesMap(dOperables, reports)
    val dOperablesKnowledge =
      entitiesIds.flatMap(id => dOperables.get(id))
        .map(DKnowledge(_))
        .toVector
    val newWarnings = knowledge.map(_.warnings).getOrElse(InferenceWarnings.empty)
    val newKnowledge = Some(NodeInferenceResult(dOperablesKnowledge, newWarnings, Vector()))
    NodeStateWithResults(nodeState.finish(entitiesIds, results), dOperables, newKnowledge)
  }
}

object NodeStateWithResults {

  def draft: NodeStateWithResults =
    NodeStateWithResults(NodeState.draft, Map(), None)
}
