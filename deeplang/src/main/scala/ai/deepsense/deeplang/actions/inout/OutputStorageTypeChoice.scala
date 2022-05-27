package ai.deepsense.deeplang.actions.inout

import ai.deepsense.deeplang.actions.readwritedataframe.googlestorage._
import ai.deepsense.deeplang.parameters.choice.Choice
import ai.deepsense.deeplang.parameters.choice.ChoiceParameter
import ai.deepsense.deeplang.parameters.library.SaveToLibraryParameter
import ai.deepsense.deeplang.parameters.BooleanParameter
import ai.deepsense.deeplang.parameters.Parameter
import ai.deepsense.deeplang.parameters.StorageType
import ai.deepsense.deeplang.parameters.StringParameter

sealed trait OutputStorageTypeChoice extends Choice {

  import OutputStorageTypeChoice._

  override val choiceOrder: List[Class[_ <: OutputStorageTypeChoice]] = List(
    classOf[File],
    classOf[Jdbc],
    classOf[GoogleSheet]
  )

}

object OutputStorageTypeChoice {

  class File() extends OutputStorageTypeChoice {

    override val name: String = StorageType.FILE.toString

    val outputFile = SaveToLibraryParameter(name = "output file", description = Some("Output file path."))

    def getOutputFile(): String = $(outputFile)

    def setOutputFile(value: String): this.type = set(outputFile, value)

    val shouldOverwrite = BooleanParameter(
      name = "overwrite",
      description = Some("Should saving a file overwrite an existing file with the same name?")
    )

    setDefault(shouldOverwrite, true)

    def getShouldOverwrite: Boolean = $(shouldOverwrite)

    def setShouldOverwrite(value: Boolean): this.type = set(shouldOverwrite, value)

    val fileFormat =
      ChoiceParameter[OutputFileFormatChoice](name = "format", description = Some("Format of the output file."))

    setDefault(fileFormat, new OutputFileFormatChoice.Csv())

    def getFileFormat(): OutputFileFormatChoice = $(fileFormat)

    def setFileFormat(value: OutputFileFormatChoice): this.type = set(fileFormat, value)

    override val params: Array[ai.deepsense.deeplang.parameters.Parameter[_]] = Array(outputFile, shouldOverwrite, fileFormat)

  }

  class Jdbc() extends OutputStorageTypeChoice with JdbcParameters {

    val shouldOverwrite = BooleanParameter(
      name = "overwrite",
      description = Some("Should saving a table overwrite an existing table with the same name?")
    )

    setDefault(shouldOverwrite, true)

    def getShouldOverwrite: Boolean = $(shouldOverwrite)

    def setShouldOverwrite(value: Boolean): this.type = set(shouldOverwrite, value)

    override val name: String = StorageType.JDBC.toString

    override val params: Array[Parameter[_]] =
      Array(jdbcUrl, jdbcDriverClassName, jdbcTableName, shouldOverwrite)

  }

  class GoogleSheet()
      extends OutputStorageTypeChoice
      with GoogleSheetParams
      with NamesIncludedParam
      with HasShouldConvertToBooleanParam {

    override val name = "Google Sheet"

    override lazy val params: Array[Parameter[_]] = Array(
      googleSheetId,
      serviceAccountCredentials,
      namesIncluded,
      shouldConvertToBoolean
    )

  }

}
