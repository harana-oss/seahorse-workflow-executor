package io.deepsense.workflowexecutor

import io.deepsense.graph.Node

object JobGroupIdForNode {

  def apply(node: Node[_]): String =
    node.id.toString

}
