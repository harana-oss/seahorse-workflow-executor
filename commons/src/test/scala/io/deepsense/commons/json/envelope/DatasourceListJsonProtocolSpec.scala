package io.deepsense.commons.json.envelope

import org.joda.time.DateTime
import org.scalatestplus.mockito.MockitoSugar
import org.scalatest.matchers.should.Matchers

import io.deepsense.api.datasourcemanager.model.AccessLevel
import io.deepsense.api.datasourcemanager.model.Datasource
import io.deepsense.api.datasourcemanager.model.DatasourceParams
import io.deepsense.api.datasourcemanager.model.DatasourceType
import io.deepsense.commons.datasource.DatasourceTestData
import io.deepsense.commons.json.datasources.DatasourceListJsonProtocol
import org.scalatest.wordspec.AnyWordSpec

class DatasourceListJsonProtocolSpec extends AnyWordSpec with MockitoSugar with Matchers {

  val uuid = "123e4567-e89b-12d3-a456-426655440000"

  val externalFile = DatasourceType.EXTERNALFILE

  val dsList = List(DatasourceTestData.multicharSeparatorLibraryCsvDatasource)

  "DatasourceJsonProtocolSpec" should {
    "serialize and deserialize single datasource" in {
      val datasourcesJson = DatasourceListJsonProtocol.toString(dsList)
      val asString        = datasourcesJson.toString
      val datasources     = DatasourceListJsonProtocol.fromString(asString)
      info(s"Datasource: $datasources, json: $asString")
      datasources should contain theSameElementsAs dsList
    }

    "serialize no datasource" in {
      val datasourcesJson = DatasourceListJsonProtocol.toString(List.empty[Datasource])
      datasourcesJson shouldBe "[]"
    }
  }

}
