package ai.deepsense.graph

import ai.deepsense.deeplang.inference.InferContext
import ai.deepsense.graph.FlowGraph.FlowNode

trait NodeInference {

  def inferKnowledge(
                      node: FlowNode,
                      context: InferContext,
                      inputInferenceForNode: NodeInferenceResult
  ): NodeInferenceResult

  def inputInferenceForNode(
                             node: FlowNode,
                             context: InferContext,
                             graphKnowledge: GraphKnowledge,
                             nodePredecessorsEndpoints: IndexedSeq[Option[Endpoint]]
  ): NodeInferenceResult

}
