package io.deepsense.deeplang

import org.scalatestplus.mockito.MockitoSugar

import io.deepsense.commons.rest.client.datasources.DatasourceClient
import io.deepsense.commons.rest.client.datasources.DatasourceInMemoryClientFactory
import io.deepsense.deeplang.catalogs.doperable.DOperableCatalog
import io.deepsense.deeplang.doperables.dataframe.DataFrameBuilder
import io.deepsense.deeplang.inference.InferContext

object MockedInferContext extends MockitoSugar {

  def apply(
      dOperableCatalog: DOperableCatalog = CatalogRecorder.resourcesCatalogRecorder.catalogs.dOperableCatalog,
      dataFrameBuilder: DataFrameBuilder = mock[DataFrameBuilder],
      innerWorkflowParser: InnerWorkflowParser = mock[InnerWorkflowParser],
      datasourceClient: DatasourceClient = new DatasourceInMemoryClientFactory(List.empty).createClient
  ): InferContext = InferContext(
    dataFrameBuilder,
    dOperableCatalog,
    innerWorkflowParser,
    datasourceClient
  )

}
