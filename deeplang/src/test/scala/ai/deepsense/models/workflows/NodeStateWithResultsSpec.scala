package ai.deepsense.models.workflows

import org.scalatestplus.mockito.MockitoSugar
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import ai.deepsense.commons.models.Entity
import ai.deepsense.deeplang.exceptions.FlowException
import ai.deepsense.deeplang.inference.InferenceWarnings
import ai.deepsense.deeplang.Knowledge
import ai.deepsense.deeplang.ActionObject
import ai.deepsense.graph.NodeInferenceResult
import ai.deepsense.reportlib.model.ReportContent

class NodeStateWithResultsSpec extends AnyWordSpec with Matchers with MockitoSugar {

  "NodeStateWithResults" should {

    "copy knowledge, keep warnings and clear errors for nonempty ActionObject list" in {
      val draftNode                                        = draftNodeState
      val (entityIds, operables, reportsMap, operablesMap) = executionResultFixture(2)
      val finished                                         = draftNode.enqueue.start.finish(entityIds, reportsMap, operablesMap)

      finished.nodeState.isCompleted shouldBe true
      finished.knowledge shouldBe Some(
        NodeInferenceResult(operables.map(Knowledge(_)).toVector, draftNode.knowledge.get.warnings, Vector())
      )
    }
    "copy knowledge, keep warnings and clear errors for empty ActionObject list" in {
      val draftNode                                        = draftNodeState
      val (entityIds, operables, reportsMap, operablesMap) = executionResultFixture(0)
      val finished                                         = draftNode.enqueue.start.finish(entityIds, reportsMap, operablesMap)

      finished.nodeState.isCompleted shouldBe true
      finished.knowledge shouldBe Some(NodeInferenceResult(Vector(), draftNode.knowledge.get.warnings, Vector()))
    }
  }

  private def draftNodeState = {
    NodeStateWithResults.draft.withKnowledge(
      NodeInferenceResult(Vector(Knowledge(mock[ActionObject])), mock[InferenceWarnings], Vector(mock[FlowException]))
    )
  }

  private def executionResultFixture(
      dOperableCount: Int
  ): (Seq[Entity.Id], Seq[ActionObject], Map[Entity.Id, ReportContent], Map[Entity.Id, ActionObject]) = {
    val entityIds    = (1 to dOperableCount).map(_ => Entity.Id.randomId).toList
    val operables    = entityIds.map(_ => mock[ActionObject])
    val reportsMap   = entityIds.map(id => id -> mock[ReportContent]).toMap
    val operablesMap = entityIds.zip(operables).toMap
    (entityIds, operables, reportsMap, operablesMap)
  }

}
