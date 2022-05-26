package ai.deepsense.deeplang.inference

import ai.deepsense.commons.rest.client.datasources.DatasourceClient
import ai.deepsense.deeplang.catalogs.DCatalog
import ai.deepsense.deeplang.doperables.dataframe.DataFrameBuilder
import ai.deepsense.models.json.graph.GraphJsonProtocol.GraphReader

/** Holds information needed by DOperations and DMethods during knowledge inference.
  * @param catalog
  *   object responsible for registering and validating the type hierarchy
  */
case class InferContext(dataFrameBuilder: DataFrameBuilder, catalog: DCatalog, datasourceClient: DatasourceClient) {

  def dOperableCatalog = catalog.operables

  def dOperationCatalog = catalog.operations

  def graphReader = new GraphReader(dOperationCatalog)

}
