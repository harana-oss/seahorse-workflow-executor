package ai.deepsense.deeplang.actions

import java.io._
import java.net.UnknownHostException
import java.util.UUID

import scala.reflect.runtime.{universe => ru}

import org.apache.spark.sql.{DataFrame => SparkDataFrame}

import ai.deepsense.commons.utils.Version
import ai.deepsense.deeplang.Action.Id
import ai.deepsense.deeplang.documentation.OperationDocumentation
import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.actions.ReadDataFrame.ReadDataFrameParameters
import ai.deepsense.deeplang.actions.exceptions.HaranaIOException
import ai.deepsense.deeplang.actions.exceptions.HaranaUnknownHostException
import ai.deepsense.deeplang.actions.inout._
import ai.deepsense.deeplang.actions.readwritedataframe.filestorage.DataFrameFromFileReader
import ai.deepsense.deeplang.actions.readwritedataframe.googlestorage.DataFrameFromGoogleSheetReader
import ai.deepsense.deeplang.actions.readwritedataframe.validators.FilePathHasValidFileScheme
import ai.deepsense.deeplang.actions.readwritedataframe.validators.ParquetSupportedOnClusterOnly
import ai.deepsense.deeplang.inference.InferContext
import ai.deepsense.deeplang.inference.InferenceWarnings
import ai.deepsense.deeplang.parameters.choice.ChoiceParameter
import ai.deepsense.deeplang.parameters.Parameter
import ai.deepsense.deeplang.parameters.Params
import ai.deepsense.deeplang.Knowledge
import ai.deepsense.deeplang.Action0To1
import ai.deepsense.deeplang.ExecutionContext

// TODO Remake this case class into class
case class ReadDataFrame()
    extends Action0To1[DataFrame]
    with ReadDataFrameParameters
    with Params
    with OperationDocumentation {

  override val id: Id = "c48dd54c-6aef-42df-ad7a-42fc59a09f0e"

  override val name: String = "Read DataFrame"

  override val description: String =
    "Reads a DataFrame from a file or database"

  override val since: Version = Version(0, 4, 0)

  val specificParams: Array[Parameter[_]] = Array(storageType)

  setDefault(storageType, new InputStorageTypeChoice.File())

  override def execute()(context: ExecutionContext): DataFrame = {
    implicit val ec = context

    try {
      val dataframe = getStorageType() match {
        case jdbcChoice: InputStorageTypeChoice.Jdbc         => readFromJdbc(jdbcChoice)
        case googleSheet: InputStorageTypeChoice.GoogleSheet =>
          DataFrameFromGoogleSheetReader.readFromGoogleSheet(
            googleSheet
          )
        case fileChoice: InputStorageTypeChoice.File         => DataFrameFromFileReader.readFromFile(fileChoice)
      }
      DataFrame.fromSparkDataFrame(dataframe)
    } catch {
      case e: UnknownHostException => throw HaranaUnknownHostException(e)
      case e: IOException          => throw HaranaIOException(e)
    }
  }

  override def inferKnowledge()(context: InferContext): (Knowledge[DataFrame], InferenceWarnings) = {
    FilePathHasValidFileScheme.validate(this)
    ParquetSupportedOnClusterOnly.validate(this)
    super.inferKnowledge()(context)
  }

  private def readFromJdbc(
      jdbcChoice: InputStorageTypeChoice.Jdbc
  )(implicit context: ExecutionContext): SparkDataFrame =
    context.sparkSQLSession.read
      .format("jdbc")
      .option("driver", jdbcChoice.getJdbcDriverClassName)
      .option("url", jdbcChoice.getJdbcUrl)
      .option("dbtable", jdbcChoice.getJdbcTableName)
      .load()

  @transient
  override lazy val tTagTO_0: ru.TypeTag[DataFrame] = ru.typeTag[DataFrame]

}

object ReadDataFrame {

  val recordDelimiterSettingName = "textinputformat.record.delimiter"

  trait ReadDataFrameParameters {
    this: Params =>

    val storageType =
      ChoiceParameter[InputStorageTypeChoice](name = "data storage type", description = Some("Storage type."))

    def getStorageType(): InputStorageTypeChoice = $(storageType)

    def setStorageType(value: InputStorageTypeChoice): this.type = set(storageType, value)

  }

  def apply(
      fileName: String,
      csvColumnSeparator: CsvParameters.ColumnSeparatorChoice,
      csvNamesIncluded: Boolean,
      csvConvertToBoolean: Boolean
  ): ReadDataFrame = {
    new ReadDataFrame()
      .setStorageType(
        new InputStorageTypeChoice.File()
          .setSourceFile(fileName)
          .setFileFormat(
            new InputFileFormatChoice.Csv()
              .setCsvColumnSeparator(csvColumnSeparator)
              .setNamesIncluded(csvNamesIncluded)
              .setShouldConvertToBoolean(csvConvertToBoolean)
          )
      )
  }

}
