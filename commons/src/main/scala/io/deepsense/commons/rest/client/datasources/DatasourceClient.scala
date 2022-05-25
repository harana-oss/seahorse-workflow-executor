package io.deepsense.commons.rest.client.datasources

import java.util.UUID

import io.deepsense.api.datasourcemanager.model.Datasource

trait DatasourceClient {
  def getDatasource(uuid: UUID): Option[Datasource]
}

trait DatasourceClientFactory {
  def createClient: DatasourceClient
}

object DatasourceTypes {
  type DatasourceId = String
  type DatasourceMap = Map[DatasourceId, Datasource]
  type DatasourceList = List[Datasource]
}
