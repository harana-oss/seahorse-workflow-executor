package ai.deepsense.workflowexecutor

import ai.deepsense.graph.Node

object JobGroupIdForNode {

  def apply(node: Node[_]): String =
    node.id.toString

}
