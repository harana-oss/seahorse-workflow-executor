package ai.deepsense.commons.rest.client.datasources

import java.net.URL
import java.util.UUID

import ai.deepsense.api.datasourcemanager.ApiClient
import ai.deepsense.api.datasourcemanager.client.DefaultApi
import ai.deepsense.api.datasourcemanager.model.Datasource
import ai.deepsense.commons.utils.Logging

class DatasourceRestClient(datasourceServerAddress: URL, userId: String) extends DatasourceClient with Logging {

  private val client = {
    val apiClient = new ApiClient()
    apiClient.setAdapterBuilder(apiClient.getAdapterBuilder.baseUrl(datasourceServerAddress.toString))
    apiClient.createService(classOf[DefaultApi])
  }

  def getDatasource(uuid: UUID): Option[Datasource] = {
    val response = client.getDatasource(userId, uuid.toString).execute()
    if (response.isSuccessful)
      Some(response.body)
    else
      None
  }

}

class DatasourceRestClientFactory(datasourceServerAddress: URL, userId: String) extends DatasourceClientFactory {

  override def createClient: DatasourceRestClient =
    new DatasourceRestClient(datasourceServerAddress, userId)

}
