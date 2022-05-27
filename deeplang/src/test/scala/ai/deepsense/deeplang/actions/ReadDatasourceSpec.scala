package ai.deepsense.deeplang.actions

import java.util.UUID

import org.mockito.Mockito._
import org.mockito.ArgumentMatchers.any

import ai.deepsense.commons.datasource.DatasourceTestData
import ai.deepsense.commons.rest.client.datasources.DatasourceClient
import ai.deepsense.deeplang.exceptions.FlowMultiException
import ai.deepsense.deeplang.inference.InferContext
import ai.deepsense.deeplang.Knowledge
import ai.deepsense.deeplang.ActionObject
import ai.deepsense.deeplang.UnitSpec

class ReadDatasourceSpec extends UnitSpec {

  "ReadDatasource.getDatasourcesId" should {
    "return declared datasources" when {
      "datasource param is defined" in {
        val someDatasourceId = UUID.randomUUID()
        val rds              = ReadDatasource().setDatasourceId(someDatasourceId)
        rds.getDatasourcesIds shouldBe Set(someDatasourceId)
      }
    }
    "return empty set" when {
      "datasource param is not defined" in {
        val rds = ReadDatasource()
        rds.getDatasourcesIds shouldBe empty
      }
    }
  }

  "ReadDatasource.inferKnowledge" should {
    "throw DeepLangMultiException" when {
      "separator contains more than two chars" in {
        val context            = mock[InferContext]
        val datasourceClient   = mock[DatasourceClient]
        when(context.datasourceClient).thenReturn(datasourceClient)
        val ds                 = DatasourceTestData.multicharSeparatorLibraryCsvDatasource
        when(datasourceClient.getDatasource(any())).thenReturn(Some(ds))
        val readDatasource     = ReadDatasource()
        readDatasource.setDatasourceId(UUID.randomUUID)
        val multilangException = intercept[FlowMultiException] {
          readDatasource.inferKnowledgeUntyped(Vector.empty[Knowledge[ActionObject]])(context)
        }
        multilangException.exceptions(0).message shouldBe
          "Parameter value `,,` does not match regex `.`."
      }
    }
  }

}
