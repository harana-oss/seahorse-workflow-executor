package io.deepsense.deeplang.doperations.inout

import io.deepsense.deeplang.doperations.readwritedataframe.googlestorage._
import io.deepsense.deeplang.params.choice.Choice
import io.deepsense.deeplang.params.choice.ChoiceParam
import io.deepsense.deeplang.params.library.SaveToLibraryParam
import io.deepsense.deeplang.params.BooleanParam
import io.deepsense.deeplang.params.Param
import io.deepsense.deeplang.params.StorageType
import io.deepsense.deeplang.params.StringParam

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

    val outputFile = SaveToLibraryParam(name = "output file", description = Some("Output file path."))

    def getOutputFile(): String = $(outputFile)

    def setOutputFile(value: String): this.type = set(outputFile, value)

    val shouldOverwrite = BooleanParam(
      name = "overwrite",
      description = Some("Should saving a file overwrite an existing file with the same name?")
    )

    setDefault(shouldOverwrite, true)

    def getShouldOverwrite: Boolean = $(shouldOverwrite)

    def setShouldOverwrite(value: Boolean): this.type = set(shouldOverwrite, value)

    val fileFormat =
      ChoiceParam[OutputFileFormatChoice](name = "format", description = Some("Format of the output file."))

    setDefault(fileFormat, new OutputFileFormatChoice.Csv())

    def getFileFormat(): OutputFileFormatChoice = $(fileFormat)

    def setFileFormat(value: OutputFileFormatChoice): this.type = set(fileFormat, value)

    override val params: Array[io.deepsense.deeplang.params.Param[_]] = Array(outputFile, shouldOverwrite, fileFormat)

  }

  class Jdbc() extends OutputStorageTypeChoice with JdbcParameters {

    val shouldOverwrite = BooleanParam(
      name = "overwrite",
      description = Some("Should saving a table overwrite an existing table with the same name?")
    )

    setDefault(shouldOverwrite, true)

    def getShouldOverwrite: Boolean = $(shouldOverwrite)

    def setShouldOverwrite(value: Boolean): this.type = set(shouldOverwrite, value)

    override val name: String = StorageType.JDBC.toString

    override val params: Array[Param[_]] =
      Array(jdbcUrl, jdbcDriverClassName, jdbcTableName, shouldOverwrite)

  }

  class GoogleSheet()
      extends OutputStorageTypeChoice
      with GoogleSheetParams
      with NamesIncludedParam
      with HasShouldConvertToBooleanParam {

    override val name = "Google Sheet"

    override lazy val params: Array[Param[_]] = Array(
      googleSheetId,
      serviceAccountCredentials,
      namesIncluded,
      shouldConvertToBoolean
    )

  }

}
