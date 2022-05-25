package io.deepsense.deeplang.doperations

import java.io.IOException
import java.util.Properties

import scala.reflect.runtime.{universe => ru}
import io.deepsense.commons.utils.Version
import io.deepsense.deeplang.DOperation.Id
import io.deepsense.deeplang._
import io.deepsense.deeplang.documentation.OperationDocumentation
import io.deepsense.deeplang.doperables.dataframe.DataFrame
import io.deepsense.deeplang.doperations.exceptions.DeepSenseIOException
import io.deepsense.deeplang.doperations.inout._
import io.deepsense.deeplang.doperations.readwritedataframe.filestorage.DataFrameToFileWriter
import io.deepsense.deeplang.doperations.readwritedataframe.googlestorage.DataFrameToGoogleSheetWriter
import io.deepsense.deeplang.doperations.readwritedataframe.validators.{FilePathHasValidFileScheme, ParquetSupportedOnClusterOnly}
import io.deepsense.deeplang.inference.{InferContext, InferenceWarnings}
import io.deepsense.deeplang.params.choice.ChoiceParam
import io.deepsense.deeplang.params.{Param, Params}
import org.apache.spark.sql.SaveMode

class WriteDataFrame()
  extends DOperation1To0[DataFrame]
  with Params
  with OperationDocumentation {

  override val id: Id = "9e460036-95cc-42c5-ba64-5bc767a40e4e"
  override val name: String = "Write DataFrame"
  override val description: String = "Writes a DataFrame to a file or database"

  override val since: Version = Version(0, 4, 0)

  @transient
  override lazy val tTagTI_0: ru.TypeTag[DataFrame] = ru.typeTag[DataFrame]

  val storageType = ChoiceParam[OutputStorageTypeChoice](
    name = "data storage type",
    description = Some("Storage type."))

  def getStorageType(): OutputStorageTypeChoice = $(storageType)
  def setStorageType(value: OutputStorageTypeChoice): this.type = set(storageType, value)

  val params: Array[Param[_]] = Array(storageType)
  setDefault(storageType, new OutputStorageTypeChoice.File())

  override def execute(dataFrame: DataFrame)(context: ExecutionContext): Unit = {
    import OutputStorageTypeChoice._
    try {
      getStorageType() match {
        case jdbcChoice: Jdbc => writeToJdbc(jdbcChoice, context, dataFrame)
        case googleSheetChoice: GoogleSheet => DataFrameToGoogleSheetWriter.writeToGoogleSheet(
          googleSheetChoice, context, dataFrame
        )
        case fileChoice: File => DataFrameToFileWriter.writeToFile(fileChoice, context, dataFrame)
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
      dataFrame: DataFrame): Unit = {
    val properties = new Properties()
    properties.setProperty("driver", jdbcChoice.getJdbcDriverClassName)

    val jdbcUrl = jdbcChoice.getJdbcUrl
    val jdbcTableName = jdbcChoice.getJdbcTableName
    val saveMode = if (jdbcChoice.getShouldOverwrite) SaveMode.Overwrite else SaveMode.ErrorIfExists

    dataFrame.sparkDataFrame.write.mode(saveMode).jdbc(jdbcUrl, jdbcTableName, properties)
  }

  override def inferKnowledge(k0: DKnowledge[DataFrame])(context: InferContext): (Unit, InferenceWarnings) = {
    FilePathHasValidFileScheme.validate(this)
    ParquetSupportedOnClusterOnly.validate(this)
    super.inferKnowledge(k0)(context)
  }
}
