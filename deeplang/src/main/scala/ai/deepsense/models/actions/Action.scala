package ai.deepsense.models.actions

import ai.deepsense.graph.Node

/** Workflow Manager's REST API action. */
trait Action

case class LaunchAction(nodes: Option[List[Node.Id]]) extends Action

case class AbortAction(nodes: Option[List[Node.Id]]) extends Action
