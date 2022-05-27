package ai.deepsense.deeplang.actions

import java.util.UUID

import scala.reflect.runtime.{universe => ru}

import ai.deepsense.api.datasourcemanager.model._
import ai.deepsense.commons.rest.client.datasources.DatasourceClient
import ai.deepsense.commons.utils.Version
import ai.deepsense.deeplang.Action.Id
import ai.deepsense.deeplang.documentation.OperationDocumentation
import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.actions.WriteDatasource.WriteDatasourceParameters
import ai.deepsense.deeplang.actions.inout.OutputStorageTypeChoice
import ai.deepsense.deeplang.actions.readwritedatasource.FromDatasourceConverters
import ai.deepsense.deeplang.exceptions.FlowException
import ai.deepsense.deeplang.exceptions.FlowMultiException
import ai.deepsense.deeplang.inference.InferContext
import ai.deepsense.deeplang.inference.InferenceWarnings
import ai.deepsense.deeplang.parameters.datasource.DatasourceIdForWriteParameter
import ai.deepsense.deeplang.parameters.BooleanParameter
import ai.deepsense.deeplang.parameters.Parameter
import ai.deepsense.deeplang.Knowledge
import ai.deepsense.deeplang.Action1To0
import ai.deepsense.deeplang.ExecutionContext

class WriteDatasource() extends Action1To0[DataFrame] with WriteDatasourceParameters with OperationDocumentation {

  override def since: Version = Version(1, 4, 0)

  @transient
  override lazy val tTagTI_0: ru.TypeTag[DataFrame] = ru.typeTag[DataFrame]

  override val id: Id = "bf082da2-a0d9-4335-a62f-9804217a1436"

  override val name: String = "Write DataFrame"

  override val description: String = "Writes data to the data source"

  override def specificParams: Array[Parameter[_]] = Array(datasourceId, shouldOverwrite)

  setDefault(shouldOverwrite, true)

  override def getDatasourcesIds: Set[UUID] = get(datasourceId).toSet

  def setDatasourceId(value: UUID): this.type = set(datasourceId, value)

  def setDatasourceId(value: String): this.type = setDatasourceId(UUID.fromString(value))

  private def getDatasourceId() = $(datasourceId)

  private def getShouldOverwrite() = $(shouldOverwrite)

  override def execute(dataFrame: DataFrame)(context: ExecutionContext): Unit =
    createWriteDataFrameFromDatasource(context.inferContext.datasourceClient).execute(dataFrame)(context)

  override protected def inferKnowledge(k0: Knowledge[DataFrame])(context: InferContext): (Unit, InferenceWarnings) = {
    val writeDataFrame = createWriteDataFrameFromDatasource(context.datasourceClient)

    val parametersValidationErrors = writeDataFrame.validateParams
    if (parametersValidationErrors.nonEmpty)
      throw new FlowMultiException(parametersValidationErrors)
    writeDataFrame.inferKnowledge(k0)(context)
  }

  private def createWriteDataFrameFromDatasource(datasourceClient: DatasourceClient) = {
    val datasourceOpt = datasourceClient.getDatasource(getDatasourceId())
    val datasource    = checkDatasourceExists(datasourceOpt)

    val storageType: OutputStorageTypeChoice = datasource.getParams.getDatasourceType match {
      case DatasourceType.JDBC              =>
        val jdbcParams = datasource.getParams.getJdbcParams
        new OutputStorageTypeChoice.Jdbc()
          .setJdbcDriverClassName(jdbcParams.getDriver)
          .setJdbcTableName(jdbcParams.getTable)
          .setJdbcUrl(jdbcParams.getUrl)
          .setShouldOverwrite(getShouldOverwrite())
      case DatasourceType.GOOGLESPREADSHEET =>
        val googleSheetParams = datasource.getParams.getGoogleSpreadsheetParams
        new OutputStorageTypeChoice.GoogleSheet()
          .setGoogleSheetId(googleSheetParams.getGoogleSpreadsheetId)
          .setGoogleServiceAccountCredentials(googleSheetParams.getGoogleServiceAccountCredentials)
          .setNamesIncluded(googleSheetParams.getIncludeHeader)
          .setShouldConvertToBoolean(googleSheetParams.getConvert01ToBoolean)
      case DatasourceType.HDFS              =>
        FromDatasourceConverters.OutputFileStorageType
          .get(datasource.getParams.getHdfsParams)
          .setShouldOverwrite(getShouldOverwrite())
      case DatasourceType.EXTERNALFILE      => throw new FlowException("Cannot write to external file")
      case DatasourceType.LIBRARYFILE       =>
        FromDatasourceConverters.OutputFileStorageType
          .get(datasource.getParams.getLibraryFileParams)
          .setShouldOverwrite(getShouldOverwrite())
    }

    new WriteDataFrame().setStorageType(storageType)
  }

  private def checkDatasourceExists(datasourceOpt: Option[Datasource]) = datasourceOpt match {
    case Some(datasource) => datasource
    case None             => throw new FlowException(s"Datasource with id = ${getDatasourceId()} not found")
  }

}

object WriteDatasource {

  def apply(): WriteDatasource = new WriteDatasource

  trait WriteDatasourceParameters {

    val datasourceId = DatasourceIdForWriteParameter(name = "data source", description = None)

    val shouldOverwrite = BooleanParameter(
      name = "overwrite",
      description = Some("Should data be overwritten?")
    )

  }

}
