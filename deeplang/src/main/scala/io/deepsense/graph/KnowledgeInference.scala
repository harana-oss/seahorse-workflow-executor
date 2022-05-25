package io.deepsense.graph

import io.deepsense.deeplang._
import io.deepsense.deeplang.inference.{InferContext, InferenceWarnings}
import io.deepsense.graph.GraphKnowledge.InferenceErrors

case class SinglePortKnowledgeInferenceResult(
  knowledge: DKnowledge[DOperable],
  warnings: InferenceWarnings,
  errors: InferenceErrors)

trait KnowledgeInference {
  self: TopologicallySortable[DOperation] with NodeInference =>

  /**
   * @return A graph knowledge with inferred results for every node.
   */
  def inferKnowledge(
      context: InferContext,
      initialKnowledge: GraphKnowledge): GraphKnowledge = {

    val sorted = topologicallySorted.getOrElse(throw CyclicGraphException())
    sorted
      .filterNot(node => initialKnowledge.containsNodeKnowledge(node.id))
      .foldLeft(initialKnowledge)(
        (knowledge, node) => {
          val nodeInferenceResult = inferKnowledge(
            node,
            context,
            inputInferenceForNode(
              node,
              context,
              knowledge,
              predecessors(node.id)))
          val innerWorkflowGraphKnowledge = node.value.inferGraphKnowledgeForInnerWorkflow(context)
          knowledge
            .addInference(node.id, nodeInferenceResult)
            .addInference(innerWorkflowGraphKnowledge)
        }
      )
  }
}
