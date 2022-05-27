package ai.deepsense.deeplang

import org.scalatestplus.mockito.MockitoSugar

import ai.deepsense.commons.rest.client.datasources.DatasourceClient
import ai.deepsense.commons.rest.client.datasources.DatasourceInMemoryClientFactory
import ai.deepsense.deeplang.catalogs.FlowCatalog
import ai.deepsense.deeplang.catalogs.actionobjects.ActionObjectCatalog
import ai.deepsense.deeplang.catalogs.actions.ActionCatalog
import ai.deepsense.deeplang.actionobjects.dataframe.DataFrameBuilder
import ai.deepsense.deeplang.inference.InferContext

object MockedInferContext extends MockitoSugar {

  def apply(
             dOperableCatalog: ActionObjectCatalog = CatalogRecorder.resourcesCatalogRecorder.catalogs.operables,
             dataFrameBuilder: DataFrameBuilder = mock[DataFrameBuilder],
             dOperationsCatalog: ActionCatalog = mock[ActionCatalog],
             datasourceClient: DatasourceClient = new DatasourceInMemoryClientFactory(List.empty).createClient
  ): InferContext = {
    val catalogPair = FlowCatalog(dOperableCatalog, dOperationsCatalog)
    InferContext(
      dataFrameBuilder,
      catalogPair,
      datasourceClient
    )
  }

}
