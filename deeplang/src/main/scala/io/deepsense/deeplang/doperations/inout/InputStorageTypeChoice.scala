package io.deepsense.deeplang.doperations.inout

import io.deepsense.deeplang.params.choice.{Choice, ChoiceParam}
import io.deepsense.deeplang.params.library.LoadFromLibraryParam
import io.deepsense.deeplang.params.{Param, StorageType}

sealed trait InputStorageTypeChoice extends Choice {
  import InputStorageTypeChoice._

  override val choiceOrder: List[Class[_ <: InputStorageTypeChoice]] = List(
    classOf[File],
    classOf[Jdbc],
    classOf[GoogleSheet]
  )
}

object InputStorageTypeChoice {

  class File extends InputStorageTypeChoice {

    override val name: String = StorageType.FILE.toString

    val sourceFile = LoadFromLibraryParam(
      name = "source",
      description = Some("Path to the DataFrame file."))

    def getSourceFile(): String = $(sourceFile)
    def setSourceFile(value: String): this.type = set(sourceFile, value)

    val fileFormat = ChoiceParam[InputFileFormatChoice](
      name = "format",
      description = Some("Format of the input file."))
    setDefault(fileFormat, new InputFileFormatChoice.Csv())

    def getFileFormat(): InputFileFormatChoice = $(fileFormat)
    def setFileFormat(value: InputFileFormatChoice): this.type = set(fileFormat, value)

    override def params = Array(sourceFile, fileFormat)
  }

  class Jdbc extends InputStorageTypeChoice with JdbcParameters {

    override val name: String = StorageType.JDBC.toString
    override def params = Array(jdbcUrl, jdbcDriverClassName, jdbcTableName)

  }

  class GoogleSheet
    extends InputStorageTypeChoice
    with GoogleSheetParams
    with NamesIncludedParam
    with HasShouldConvertToBooleanParam {

    override val name: String = "Google Sheet"
    override lazy val params: Array[Param[_]] = Array(
      googleSheetId, serviceAccountCredentials, namesIncluded, shouldConvertToBoolean
    )

  }

}
