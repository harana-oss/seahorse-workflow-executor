package ai.deepsense.graph

import org.mockito.Mockito._
import org.scalatestplus.mockito.MockitoSugar
import org.scalatest.wordspec.AnyWordSpec
import ai.deepsense.deeplang.exceptions.FlowException
import org.scalatest.matchers.should.Matchers

class GraphKnowledgeSpec extends AnyWordSpec with Matchers with MockitoSugar {

  "GraphKnowledge" should {
    "return proper errors map" in {
      val node1Id                       = Node.Id.randomId
      val node2Id                       = Node.Id.randomId
      val inferenceResultsWithErrors    = mock[NodeInferenceResult]
      val errors                        = Vector(mock[FlowException], mock[FlowException])
      when(inferenceResultsWithErrors.errors).thenReturn(errors)
      val inferenceResultsWithoutErrors = mock[NodeInferenceResult]
      when(inferenceResultsWithoutErrors.errors).thenReturn(Vector.empty)

      val knowledge = GraphKnowledge()
        .addInference(node1Id, inferenceResultsWithErrors)
        .addInference(node2Id, inferenceResultsWithoutErrors)

      knowledge.errors shouldBe Map(node1Id -> errors)
    }
  }

}
