package io.deepsense.deeplang.doperations

import java.util.UUID

import org.mockito.Matchers._
import org.mockito.Mockito._

import io.deepsense.commons.datasource.DatasourceTestData
import io.deepsense.commons.rest.client.datasources.DatasourceClient
import io.deepsense.deeplang.exceptions.DeepLangMultiException
import io.deepsense.deeplang.inference.InferContext
import io.deepsense.deeplang.{DKnowledge, DOperable, UnitSpec}

class ReadDatasourceSpec extends UnitSpec {

  "ReadDatasource.getDatasourcesId" should {
    "return declared datasources" when {
      "datasource param is defined" in {
        val someDatasourceId = UUID.randomUUID()
        val rds = ReadDatasource().setDatasourceId(someDatasourceId)
        rds.getDatasourcesId shouldBe Set(someDatasourceId)
      }
    }
    "return empty set" when {
      "datasource param is not defined" in {
        val rds = ReadDatasource()
        rds.getDatasourcesId shouldBe empty
      }
    }
  }

  "ReadDatasource.inferKnowledge" should {
    "throw DeepLangMultiException" when {
      "separator contains more than two chars" in {
        val context = mock[InferContext]
        val datasourceClient = mock[DatasourceClient]
        when(context.datasourceClient).thenReturn(datasourceClient)
        val ds = DatasourceTestData.multicharSeparatorLibraryCsvDatasource
        when(datasourceClient.getDatasource(any())).thenReturn(Some(ds))
        val readDatasource = ReadDatasource()
        readDatasource.setDatasourceId(UUID.randomUUID)
        val multilangException = intercept[DeepLangMultiException] {
          readDatasource.inferKnowledgeUntyped(Vector.empty[DKnowledge[DOperable]])(context)
        }
        multilangException.exceptions(0).message shouldBe
            "Parameter value `,,` does not match regex `.`."
      }
    }
  }

}
