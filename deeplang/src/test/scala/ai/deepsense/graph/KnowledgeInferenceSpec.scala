package ai.deepsense.graph

import org.mockito.ArgumentMatchers.{ eq => isEqualTo, _}
import org.mockito.Mockito._
import org.scalatest.BeforeAndAfter
import ai.deepsense.deeplang.Action
import ai.deepsense.deeplang.inference.InferContext
import ai.deepsense.deeplang.inference.InferenceWarnings
import ai.deepsense.graph.FlowGraph.FlowNode
import org.mockito.ArgumentMatchers.any

class KnowledgeInferenceSpec extends AbstractInferenceSpec with BeforeAndAfter {

  val topologicallySortedMock = mock[TopologicallySortable[Action]]

  val nodeInferenceMock = mock[NodeInference]

  val graph = DirectedGraphWithSomeLogicMocked(topologicallySortedMock, nodeInferenceMock)

  before {
    reset(topologicallySortedMock)
    reset(nodeInferenceMock)
  }

  "Graph" should {
    "infer type knowledge" when {
      "graph is valid" in {
        val topologicallySortedNodes    = List(
          nodeCreateA1,
          nodeA1ToA,
          nodeAToA1A2,
          nodeA1A2ToFirst
        )
        val nodeInferenceResultForNodes = List(
          NodeInferenceResult(Vector(knowledgeA1)),
          NodeInferenceResult(Vector(knowledgeA1, knowledgeA2)),
          NodeInferenceResult(Vector(knowledgeA1)),
          NodeInferenceResult(Vector(knowledgeA1), warnings = mock[InferenceWarnings])
        )
        when(topologicallySortedMock.topologicallySorted).thenReturn(Some(topologicallySortedNodes))
        topologicallySortedNodes.zip(nodeInferenceResultForNodes).foreach {
          case (node: FlowNode, result: NodeInferenceResult) =>
            nodeInferenceMockShouldInferResultForNode(node, result)
        }

        val graphKnowledge         = graph.inferKnowledge(mock[InferContext], GraphKnowledge())
        val graphKnowledgeExpected = topologicallySortedNodes
          .map(_.id)
          .zip(nodeInferenceResultForNodes)
          .toMap
        graphKnowledge.resultsMap should contain theSameElementsAs graphKnowledgeExpected
      }
    }

    "infer only unprovided knowledge" when {
      "given initial knowledge" in {

        val initialKnowledge = Map(
          nodeCreateA1.id -> NodeInferenceResult(Vector(knowledgeA1)),
          nodeA1ToA.id    -> NodeInferenceResult(Vector(knowledgeA1, knowledgeA2))
        )

        val knowledgeToInfer = Map(
          nodeAToA1A2.id     -> NodeInferenceResult(Vector(knowledgeA1)),
          nodeA1A2ToFirst.id -> NodeInferenceResult(Vector(knowledgeA1), warnings = mock[InferenceWarnings])
        )

        val nodesWithKnowledge  = List(nodeCreateA1, nodeA1ToA)
        val nodesToInfer        = List(nodeAToA1A2, nodeA1A2ToFirst)
        val topologicallySorted = nodesWithKnowledge ++ nodesToInfer

        when(topologicallySortedMock.topologicallySorted).thenReturn(Some(topologicallySorted))

        nodesWithKnowledge.foreach((node: FlowNode) => nodeInferenceMockShouldThrowForNode(node))
        nodesToInfer.foreach((node: FlowNode) =>
          nodeInferenceMockShouldInferResultForNode(node, knowledgeToInfer(node.id))
        )

        val expectedKnowledge = initialKnowledge ++ knowledgeToInfer
        val graphKnowledge    = graph.inferKnowledge(mock[InferContext], GraphKnowledge(initialKnowledge))

        graphKnowledge.resultsMap should contain theSameElementsAs expectedKnowledge
      }
    }

    "throw an exception" when {
      "graph contains cycle" in {
        intercept[CyclicGraphException] {
          val topologicallySortedMock = mock[TopologicallySortable[Action]]
          when(topologicallySortedMock.topologicallySorted).thenReturn(None)
          val graph                   = DirectedGraphWithSomeLogicMocked(
            topologicallySortedMock,
            nodeInferenceMock
          )
          graph.inferKnowledge(mock[InferContext], GraphKnowledge())
        }
        ()
      }
    }
  }

  def nodeInferenceMockShouldInferResultForNode(
                                                 nodeCreateA1: FlowNode,
                                                 nodeCreateA1InferenceResult: NodeInferenceResult
  ): Unit =
    when(nodeInferenceMock.inferKnowledge(isEqualTo(nodeCreateA1), any[InferContext], any[NodeInferenceResult]))
      .thenReturn(nodeCreateA1InferenceResult)

  def nodeInferenceMockShouldThrowForNode(node: FlowNode): Unit =
    when(nodeInferenceMock.inferKnowledge(isEqualTo(node), any[InferContext], any[NodeInferenceResult]))
      .thenThrow(new RuntimeException("Inference should not be called for node " + node.id))

  case class DirectedGraphWithSomeLogicMocked(
                                               val topologicallySortableMock: TopologicallySortable[Action],
                                               val nodeInferenceMock: NodeInference
  ) extends TopologicallySortable[Action]
      with KnowledgeInference
      with NodeInference {

    override def inferKnowledge(
                                 node: FlowNode,
                                 context: InferContext,
                                 inputInferenceForNode: NodeInferenceResult
    ): NodeInferenceResult =
      nodeInferenceMock.inferKnowledge(node, context, inputInferenceForNode)

    override def inputInferenceForNode(
                                        node: FlowNode,
                                        context: InferContext,
                                        graphKnowledge: GraphKnowledge,
                                        nodePredecessorsEndpoints: IndexedSeq[Option[Endpoint]]
    ): NodeInferenceResult = {
      nodeInferenceMock.inputInferenceForNode(
        node,
        context,
        graphKnowledge,
        nodePredecessorsEndpoints
      )
    }

    override def topologicallySorted: Option[List[FlowNode]] =
      topologicallySortableMock.topologicallySorted

    override def node(id: Node.Id): FlowNode = topologicallySortableMock.node(id)

    override def allPredecessorsOf(id: Node.Id): Set[FlowNode] =
      topologicallySortableMock.allPredecessorsOf(id)

    override def predecessors(id: Node.Id): IndexedSeq[Option[Endpoint]] =
      topologicallySortableMock.predecessors(id)

    override def successors(id: Node.Id): IndexedSeq[Set[Endpoint]] =
      topologicallySortableMock.successors(id)

    override def edges: Set[Edge] = ???

    override def nodes: Set[FlowNode] = ???

  }

}
