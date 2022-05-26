package ai.deepsense.deeplang.doperations

import java.io.IOException
import java.util.Properties

import scala.reflect.runtime.{universe => ru}
import ai.deepsense.commons.utils.Version
import ai.deepsense.deeplang.DOperation.Id
import ai.deepsense.deeplang._
import ai.deepsense.deeplang.documentation.OperationDocumentation
import ai.deepsense.deeplang.doperables.dataframe.DataFrame
import ai.deepsense.deeplang.doperations.exceptions.DeepSenseIOException
import ai.deepsense.deeplang.doperations.inout._
import ai.deepsense.deeplang.doperations.readwritedataframe.filestorage.DataFrameToFileWriter
import ai.deepsense.deeplang.doperations.readwritedataframe.googlestorage.DataFrameToGoogleSheetWriter
import ai.deepsense.deeplang.doperations.readwritedataframe.validators.FilePathHasValidFileScheme
import ai.deepsense.deeplang.doperations.readwritedataframe.validators.ParquetSupportedOnClusterOnly
import ai.deepsense.deeplang.inference.InferContext
import ai.deepsense.deeplang.inference.InferenceWarnings
import ai.deepsense.deeplang.params.choice.ChoiceParam
import ai.deepsense.deeplang.params.Param
import ai.deepsense.deeplang.params.Params
import org.apache.spark.sql.SaveMode

class WriteDataFrame() extends DOperation1To0[DataFrame] with Params with OperationDocumentation {

  override val id: Id = "9e460036-95cc-42c5-ba64-5bc767a40e4e"

  override val name: String = "Write DataFrame"

  override val description: String = "Writes a DataFrame to a file or database"

  override val since: Version = Version(0, 4, 0)

  @transient
  override lazy val tTagTI_0: ru.TypeTag[DataFrame] = ru.typeTag[DataFrame]

  val storageType =
    ChoiceParam[OutputStorageTypeChoice](name = "data storage type", description = Some("Storage type."))

  def getStorageType(): OutputStorageTypeChoice = $(storageType)

  def setStorageType(value: OutputStorageTypeChoice): this.type = set(storageType, value)

  val specificParams: Array[Param[_]] = Array(storageType)

  setDefault(storageType, new OutputStorageTypeChoice.File())

  override def execute(dataFrame: DataFrame)(context: ExecutionContext): Unit = {
    import OutputStorageTypeChoice._
    try {
      getStorageType() match {
        case jdbcChoice: Jdbc               => writeToJdbc(jdbcChoice, context, dataFrame)
        case googleSheetChoice: GoogleSheet =>
          DataFrameToGoogleSheetWriter.writeToGoogleSheet(
            googleSheetChoice,
            context,
            dataFrame
          )
        case fileChoice: File               => DataFrameToFileWriter.writeToFile(fileChoice, context, dataFrame)
      }
    } catch {
      case e: IOException =>
        logger.error(s"WriteDataFrame error. Could not write file to designated storage", e)
        throw DeepSenseIOException(e)
    }
  }

  private def writeToJdbc(
      jdbcChoice: OutputStorageTypeChoice.Jdbc,
      context: ExecutionContext,
      dataFrame: DataFrame
  ): Unit = {
    val properties = new Properties()
    properties.setProperty("driver", jdbcChoice.getJdbcDriverClassName)

    val jdbcUrl       = jdbcChoice.getJdbcUrl
    val jdbcTableName = jdbcChoice.getJdbcTableName
    val saveMode      = if (jdbcChoice.getShouldOverwrite) SaveMode.Overwrite else SaveMode.ErrorIfExists

    dataFrame.sparkDataFrame.write.mode(saveMode).jdbc(jdbcUrl, jdbcTableName, properties)
  }

  override def inferKnowledge(k0: DKnowledge[DataFrame])(context: InferContext): (Unit, InferenceWarnings) = {
    FilePathHasValidFileScheme.validate(this)
    ParquetSupportedOnClusterOnly.validate(this)
    super.inferKnowledge(k0)(context)
  }

}
