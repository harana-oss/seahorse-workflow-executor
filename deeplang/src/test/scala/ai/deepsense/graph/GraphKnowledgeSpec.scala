package ai.deepsense.graph

import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatest.Matchers
import org.scalatest.WordSpec

import ai.deepsense.deeplang.exceptions.DeepLangException

class GraphKnowledgeSpec extends WordSpec with MockitoSugar with Matchers {

  "GraphKnowledge" should {
    "return proper errors map" in {
      val node1Id                       = Node.Id.randomId
      val node2Id                       = Node.Id.randomId
      val inferenceResultsWithErrors    = mock[NodeInferenceResult]
      val errors                        = Vector(mock[DeepLangException], mock[DeepLangException])
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
