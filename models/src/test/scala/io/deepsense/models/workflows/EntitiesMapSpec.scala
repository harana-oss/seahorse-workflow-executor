package io.deepsense.models.workflows

import org.scalatest.mockito.MockitoSugar
import org.scalatest.{Matchers, WordSpec}

import io.deepsense.commons.models.Entity
import io.deepsense.deeplang.doperables.dataframe.DataFrame
import io.deepsense.reportlib.model.ReportContent

class EntitiesMapSpec
  extends WordSpec
  with Matchers
  with MockitoSugar {

  "EntitiesMap" should {
    "be correctly created from results and reports" in {

      val entity1Id = Entity.Id.randomId
      val doperable1 = new DataFrame()
      val report1 = mock[ReportContent]

      val entity2Id = Entity.Id.randomId
      val doperable2 = new DataFrame()

      val results = Map(entity1Id -> doperable1, entity2Id -> doperable2)
      val reports = Map(entity1Id -> report1)

      EntitiesMap(results, reports) shouldBe EntitiesMap(Map(
        entity1Id -> EntitiesMap.Entry(
          "io.deepsense.deeplang.doperables.dataframe.DataFrame", Some(report1)),
        entity2Id -> EntitiesMap.Entry(
          "io.deepsense.deeplang.doperables.dataframe.DataFrame", None)
      ))
    }
  }
}
