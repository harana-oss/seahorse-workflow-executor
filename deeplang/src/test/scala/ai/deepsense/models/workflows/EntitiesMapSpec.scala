package ai.deepsense.models.workflows

import org.scalatestplus.mockito.MockitoSugar
import org.scalatest.matchers.should.Matchers
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import ai.deepsense.commons.models.Entity
import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.reportlib.model.ReportContent

class EntitiesMapSpec extends AnyWordSpec with Matchers with MockitoSugar {

  "EntitiesMap" should {
    "be correctly created from results and reports" in {

      val entity1Id  = Entity.Id.randomId
      val actionObject1 = new DataFrame()
      val report1    = mock[ReportContent]

      val entity2Id  = Entity.Id.randomId
      val actionObject2 = new DataFrame()

      val results = Map(entity1Id -> actionObject1, entity2Id -> actionObject2)
      val reports = Map(entity1Id -> report1)

      EntitiesMap(results, reports) shouldBe EntitiesMap(
        Map(
          entity1Id -> EntitiesMap.Entry("ai.deepsense.deeplang.actionobjects.dataframe.DataFrame", Some(report1)),
          entity2Id -> EntitiesMap.Entry("ai.deepsense.deeplang.actionobjects.dataframe.DataFrame", None)
        )
      )
    }
  }

}
