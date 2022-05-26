package ai.deepsense.deeplang

import org.scalatestplus.mockito.MockitoSugar

import ai.deepsense.commons.rest.client.datasources.DatasourceClient
import ai.deepsense.commons.rest.client.datasources.DatasourceInMemoryClientFactory
import ai.deepsense.deeplang.catalogs.DCatalog
import ai.deepsense.deeplang.catalogs.doperable.DOperableCatalog
import ai.deepsense.deeplang.catalogs.doperations.DOperationsCatalog
import ai.deepsense.deeplang.doperables.dataframe.DataFrameBuilder
import ai.deepsense.deeplang.inference.InferContext

object MockedInferContext extends MockitoSugar {

  def apply(
      dOperableCatalog: DOperableCatalog = CatalogRecorder.resourcesCatalogRecorder.catalogs.operables,
      dataFrameBuilder: DataFrameBuilder = mock[DataFrameBuilder],
      dOperationsCatalog: DOperationsCatalog = mock[DOperationsCatalog],
      datasourceClient: DatasourceClient = new DatasourceInMemoryClientFactory(List.empty).createClient
  ): InferContext = {
    val catalogPair = DCatalog(dOperableCatalog, dOperationsCatalog)
    InferContext(
      dataFrameBuilder,
      catalogPair,
      datasourceClient
    )
  }

}
