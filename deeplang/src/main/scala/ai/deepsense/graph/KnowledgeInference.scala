package ai.deepsense.graph

import ai.deepsense.deeplang._
import ai.deepsense.deeplang.inference.InferContext
import ai.deepsense.deeplang.inference.InferenceWarnings
import ai.deepsense.graph.GraphKnowledge.InferenceErrors

case class SinglePortKnowledgeInferenceResult(
                                               knowledge: Knowledge[ActionObject],
                                               warnings: InferenceWarnings,
                                               errors: InferenceErrors
)

trait KnowledgeInference {
  self: TopologicallySortable[Action] with NodeInference =>

  /** @return A graph knowledge with inferred results for every node. */
  def inferKnowledge(context: InferContext, initialKnowledge: GraphKnowledge): GraphKnowledge = {

    val sorted = topologicallySorted.getOrElse(throw CyclicGraphException())
    sorted
      .filterNot(node => initialKnowledge.containsNodeKnowledge(node.id))
      .foldLeft(initialKnowledge) { (knowledge, node) =>
        val nodeInferenceResult         =
          inferKnowledge(node, context, inputInferenceForNode(node, context, knowledge, predecessors(node.id)))
        val innerWorkflowGraphKnowledge = node.value.inferGraphKnowledgeForInnerWorkflow(context)
        knowledge
          .addInference(node.id, nodeInferenceResult)
          .addInference(innerWorkflowGraphKnowledge)
      }
  }

}
