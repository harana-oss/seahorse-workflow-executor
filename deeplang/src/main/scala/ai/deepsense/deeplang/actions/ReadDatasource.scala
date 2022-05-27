package ai.deepsense.deeplang.actions

import java.util.UUID

import scala.reflect.runtime.{universe => ru}

import ai.deepsense.api.datasourcemanager.model._
import ai.deepsense.commons.rest.client.datasources.DatasourceClient
import ai.deepsense.commons.utils.Version
import ai.deepsense.deeplang.Action.Id
import ai.deepsense.deeplang.documentation.OperationDocumentation
import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.actions.ReadDatasource.ReadDataSourceParameters
import ai.deepsense.deeplang.actions.inout.InputStorageTypeChoice
import ai.deepsense.deeplang.actions.readwritedatasource.FromDatasourceConverters
import ai.deepsense.deeplang.exceptions.FlowException
import ai.deepsense.deeplang.exceptions.FlowMultiException
import ai.deepsense.deeplang.inference.InferContext
import ai.deepsense.deeplang.inference.InferenceWarnings
import ai.deepsense.deeplang.parameters.Parameter
import ai.deepsense.deeplang.parameters.Params
import ai.deepsense.deeplang.parameters.datasource.DatasourceIdForReadParameter
import ai.deepsense.deeplang.parameters.exceptions.ParamValueNotProvidedException
import ai.deepsense.deeplang.Knowledge
import ai.deepsense.deeplang.Action0To1
import ai.deepsense.deeplang.ExecutionContext

class ReadDatasource() extends Action0To1[DataFrame] with ReadDataSourceParameters with OperationDocumentation {

  override def since: Version = Version(1, 4, 0)

  @transient
  override lazy val tTagTO_0: ru.TypeTag[DataFrame] = ru.typeTag[DataFrame]

  override val id: Id = "1a3b32f0-f56d-4c44-a396-29d2dfd43423"

  override val name: String = "Read DataFrame"

  override val description: String = "Reads data from its source to Seahorse's memory"

  override def specificParams: Array[Parameter[_]] = Array(datasourceId)

  override def getDatasourcesIds: Set[UUID] = get(datasourceId).toSet

  def setDatasourceId(value: UUID): this.type = set(datasourceId, value)

  def setDatasourceId(value: String): this.type = setDatasourceId(UUID.fromString(value))

  private def getDatasourceId() = $(datasourceId)

  override def execute()(context: ExecutionContext): DataFrame =
    createReadDataFrameFromDataSource(context.inferContext.datasourceClient).execute()(context)

  override protected def inferKnowledge()(context: InferContext): (Knowledge[DataFrame], InferenceWarnings) = {
    val readDataFrame = createReadDataFrameFromDataSource(context.datasourceClient)

    val parametersValidationErrors = readDataFrame.validateParams
    if (parametersValidationErrors.nonEmpty)
      throw new FlowMultiException(parametersValidationErrors)
    readDataFrame.inferKnowledge()(context)
  }

  private def createReadDataFrameFromDataSource(datasourceClient: DatasourceClient) = {
    val datasourceOpt = datasourceClient.getDatasource(getDatasourceId())
    val datasource    = checkDataSourceExists(datasourceOpt)

    // TODO Reduce similar code with WriteDatasource

    val storageType = datasource.getParams.getDatasourceType match {
      case DatasourceType.JDBC =>
        val jdbcParams = datasource.getParams.getJdbcParams

        val from = Option(jdbcParams.getQuery)
          .map(wrapAsSubQuery)
          .orElse(Option(jdbcParams.getTable))
          .get

        new InputStorageTypeChoice.Jdbc()
          .setJdbcDriverClassName(jdbcParams.getDriver)
          .setJdbcTableName(from)
          .setJdbcUrl(jdbcParams.getUrl)

      case DatasourceType.GOOGLESPREADSHEET =>
        val googleSheetParams = datasource.getParams.getGoogleSpreadsheetParams
        new InputStorageTypeChoice.GoogleSheet()
          .setGoogleSheetId(googleSheetParams.getGoogleSpreadsheetId)
          .setGoogleServiceAccountCredentials(googleSheetParams.getGoogleServiceAccountCredentials)
          .setNamesIncluded(googleSheetParams.getIncludeHeader)
          .setShouldConvertToBoolean(googleSheetParams.getConvert01ToBoolean)
      case DatasourceType.HDFS              =>
        FromDatasourceConverters.InputFileStorageType.get(datasource.getParams.getHdfsParams)
      case DatasourceType.EXTERNALFILE      =>
        FromDatasourceConverters.InputFileStorageType.get(datasource.getParams.getExternalFileParams)
      case DatasourceType.LIBRARYFILE       =>
        FromDatasourceConverters.InputFileStorageType.get(datasource.getParams.getLibraryFileParams)
    }

    new ReadDataFrame().setStorageType(storageType)
  }

  private def checkDataSourceExists(datasourceOpt: Option[Datasource]) = datasourceOpt match {
    case Some(datasource) => datasource
    case None             => throw new FlowException(s"Datasource with id = ${getDatasourceId()} not found")
  }

  private def wrapAsSubQuery(query: String): String =
    // Note that oracle alias column name cannot exceed 30 characters, hence .take(16).
    // "tmp" prefix is needed so that name always starts with character.
    s"($query) tmp${UUID.randomUUID.toString.replace("-", "").take(16)}"

}

object ReadDatasource {

  def apply(): ReadDatasource = new ReadDatasource

  trait ReadDataSourceParameters {

    val datasourceId = DatasourceIdForReadParameter(name = "data source", description = None)

  }

}
