package io.deepsense.commons.rest.client.datasources

import java.util.UUID

import io.deepsense.api.datasourcemanager.model.Datasource
import io.deepsense.commons.rest.client.datasources.DatasourceTypes.DatasourceId
import io.deepsense.commons.rest.client.datasources.DatasourceTypes.DatasourceList
import io.deepsense.commons.rest.client.datasources.DatasourceTypes.DatasourceMap
import io.deepsense.commons.utils.CollectionExtensions

class DatasourceInMemoryClient(datasourceList: DatasourceList) extends DatasourceClient {

  import CollectionExtensions._

  def getDatasource(uuid: UUID): Option[Datasource] =
    datasourceMap.get(uuid.toString)

  private val datasourceMap = datasourceList.lookupBy(_.getId)

}

class DatasourceInMemoryClientFactory(datasourceMap: DatasourceList) extends DatasourceClientFactory {

  override def createClient: DatasourceClient =
    new DatasourceInMemoryClient(datasourceMap)

}
