package io.deepsense.deeplang.doperations

import java.util.UUID

import scala.reflect.runtime.{universe => ru}

import io.deepsense.api.datasourcemanager.model._
import io.deepsense.commons.rest.client.datasources.DatasourceClient
import io.deepsense.commons.utils.Version
import io.deepsense.deeplang.DOperation.Id
import io.deepsense.deeplang.documentation.OperationDocumentation
import io.deepsense.deeplang.doperables.dataframe.DataFrame
import io.deepsense.deeplang.doperations.WriteDatasource.WriteDatasourceParameters
import io.deepsense.deeplang.doperations.inout.OutputStorageTypeChoice
import io.deepsense.deeplang.doperations.readwritedatasource.FromDatasourceConverters
import io.deepsense.deeplang.exceptions.DeepLangException
import io.deepsense.deeplang.exceptions.DeepLangMultiException
import io.deepsense.deeplang.inference.InferContext
import io.deepsense.deeplang.inference.InferenceWarnings
import io.deepsense.deeplang.params.datasource.DatasourceIdForWriteParam
import io.deepsense.deeplang.params.BooleanParam
import io.deepsense.deeplang.params.Param
import io.deepsense.deeplang.DKnowledge
import io.deepsense.deeplang.DOperation1To0
import io.deepsense.deeplang.ExecutionContext

class WriteDatasource() extends DOperation1To0[DataFrame] with WriteDatasourceParameters with OperationDocumentation {

  override def since: Version = Version(1, 4, 0)

  @transient
  override lazy val tTagTI_0: ru.TypeTag[DataFrame] = ru.typeTag[DataFrame]

  override val id: Id = "bf082da2-a0d9-4335-a62f-9804217a1436"

  override val name: String = "Write DataFrame"

  override val description: String = "Writes data to the data source"

  override def params: Array[Param[_]] = Array(datasourceId, shouldOverwrite)

  setDefault(shouldOverwrite, true)

  override def getDatasourcesId: Set[UUID] = get(datasourceId).toSet

  def setDatasourceId(value: UUID): this.type = set(datasourceId, value)

  def setDatasourceId(value: String): this.type = setDatasourceId(UUID.fromString(value))

  private def getDatasourceId() = $(datasourceId)

  private def getShouldOverwrite() = $(shouldOverwrite)

  override def execute(dataFrame: DataFrame)(context: ExecutionContext): Unit =
    createWriteDataFrameFromDatasource(context.inferContext.datasourceClient).execute(dataFrame)(context)

  override protected def inferKnowledge(k0: DKnowledge[DataFrame])(context: InferContext): (Unit, InferenceWarnings) = {
    val writeDataFrame = createWriteDataFrameFromDatasource(context.datasourceClient)

    val parametersValidationErrors = writeDataFrame.validateParams
    if (parametersValidationErrors.nonEmpty)
      throw new DeepLangMultiException(parametersValidationErrors)
    writeDataFrame.inferKnowledge(k0)(context)
  }

  private def createWriteDataFrameFromDatasource(datasourceClient: DatasourceClient) = {
    val datasourceOpt = datasourceClient.getDatasource(getDatasourceId())
    val datasource    = checkDatasourceExists(datasourceOpt)

    val storageType: OutputStorageTypeChoice = datasource.getParams.getDatasourceType match {
      case DatasourceType.JDBC =>
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
      case DatasourceType.HDFS =>
        FromDatasourceConverters.OutputFileStorageType
          .get(datasource.getParams.getHdfsParams)
          .setShouldOverwrite(getShouldOverwrite())
      case DatasourceType.EXTERNALFILE => throw new DeepLangException("Cannot write to external file")
      case DatasourceType.LIBRARYFILE =>
        FromDatasourceConverters.OutputFileStorageType
          .get(datasource.getParams.getLibraryFileParams)
          .setShouldOverwrite(getShouldOverwrite())
    }

    new WriteDataFrame().setStorageType(storageType)
  }

  private def checkDatasourceExists(datasourceOpt: Option[Datasource]) = datasourceOpt match {
    case Some(datasource) => datasource
    case None             => throw new DeepLangException(s"Datasource with id = ${getDatasourceId()} not found")
  }

}

object WriteDatasource {

  def apply(): WriteDatasource = new WriteDatasource

  trait WriteDatasourceParameters {

    val datasourceId = DatasourceIdForWriteParam(name = "data source", description = None)

    val shouldOverwrite = BooleanParam(
      name = "overwrite",
      description = Some("Should data be overwritten?")
    )

  }

}
