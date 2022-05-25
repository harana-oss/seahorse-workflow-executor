package io.deepsense.deeplang.doperations

import java.util.UUID

import io.deepsense.deeplang.UnitSpec

class WriteDatasourceSpec extends UnitSpec {

  "WriteDatasource.getDatasourcesId" should {
    "return declared datasources" when {
      "datasource param is defined" in {
        val someDatasourceId = UUID.randomUUID()
        val wds = WriteDatasource().setDatasourceId(someDatasourceId)
        wds.getDatasourcesId shouldBe Set(someDatasourceId)
      }
    }
    "return empty set" when {
      "datasource param is not defined" in {
        val wds = ReadDatasource()
        wds.getDatasourcesId shouldBe empty
      }
    }
  }
}
