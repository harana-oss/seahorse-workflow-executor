package io.deepsense.commons.json.envelope

import org.joda.time.DateTime
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{Matchers, WordSpec}

import io.deepsense.api.datasourcemanager.model.{AccessLevel, Datasource, DatasourceParams, DatasourceType}
import io.deepsense.commons.datasource.DatasourceTestData
import io.deepsense.commons.json.datasources.DatasourceListJsonProtocol

class DatasourceListJsonProtocolSpec
  extends WordSpec
  with MockitoSugar
  with Matchers {

  val uuid = "123e4567-e89b-12d3-a456-426655440000"
  val externalFile = DatasourceType.EXTERNALFILE

  val dsList = List(DatasourceTestData.multicharSeparatorLibraryCsvDatasource)

  "DatasourceJsonProtocolSpec" should {
    "serialize and deserialize single datasource" in {
      val datasourcesJson = DatasourceListJsonProtocol.toString(dsList)
      val asString = datasourcesJson.toString
      val datasources = DatasourceListJsonProtocol.fromString(asString)
      info(s"Datasource: $datasources, json: $asString")
      datasources should contain theSameElementsAs dsList
    }

    "serialize no datasource" in {
      val datasourcesJson = DatasourceListJsonProtocol.toString(List.empty[Datasource])
      datasourcesJson shouldBe "[]"
    }
  }
}
