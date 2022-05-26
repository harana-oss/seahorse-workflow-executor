package ai.deepsense.graph

import ai.deepsense.deeplang.inference.InferContext
import ai.deepsense.graph.DeeplangGraph.DeeplangNode

trait NodeInference {

  def inferKnowledge(
      node: DeeplangNode,
      context: InferContext,
      inputInferenceForNode: NodeInferenceResult
  ): NodeInferenceResult

  def inputInferenceForNode(
      node: DeeplangNode,
      context: InferContext,
      graphKnowledge: GraphKnowledge,
      nodePredecessorsEndpoints: IndexedSeq[Option[Endpoint]]
  ): NodeInferenceResult

}
