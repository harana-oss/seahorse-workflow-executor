package io.deepsense.deeplang.inference

import io.deepsense.commons.rest.client.datasources.DatasourceClient
import io.deepsense.deeplang.InnerWorkflowParser
import io.deepsense.deeplang.catalogs.doperable.DOperableCatalog
import io.deepsense.deeplang.doperables.dataframe.DataFrameBuilder

/** Holds information needed by DOperations and DMethods during knowledge inference.
  * @param dOperableCatalog
  *   object responsible for registering and validating the type hierarchy
  */
case class InferContext(
    dataFrameBuilder: DataFrameBuilder,
    dOperableCatalog: DOperableCatalog,
    innerWorkflowParser: InnerWorkflowParser,
    datasourceClient: DatasourceClient
)
