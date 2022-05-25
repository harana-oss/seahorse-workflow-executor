package io.deepsense.deeplang.doperations.readwritedatasource

import io.deepsense.api.datasourcemanager.model._
import io.deepsense.deeplang.doperations.inout.CsvParameters.ColumnSeparatorChoice
import io.deepsense.deeplang.doperations.inout.{InputFileFormatChoice, InputStorageTypeChoice, OutputFileFormatChoice, OutputStorageTypeChoice}

object FromDatasourceConverters {

  trait DatasourceParams {
    def path: String
    def fileFormat: FileFormat
    def csvFileFormatParams: CsvFileFormatParams
  }

  implicit def hdfsParamsToInputStorageParams(params: HdfsParams): DatasourceParams =
    new DatasourceParams {
      override def path: String = params.getHdfsPath
      override def fileFormat: FileFormat = params.getFileFormat
      override def csvFileFormatParams: CsvFileFormatParams = params.getCsvFileFormatParams
    }

  implicit def externalFileParamsToInputStorageParams(params: ExternalFileParams): DatasourceParams =
    new DatasourceParams {
      override def path: String = params.getUrl
      override def fileFormat: FileFormat = params.getFileFormat
      override def csvFileFormatParams: CsvFileFormatParams = params.getCsvFileFormatParams
    }

  implicit def libraryParamsToInputStorageParams(params: LibraryFileParams): DatasourceParams =
    new DatasourceParams {
      override def path: String = params.getLibraryPath
      override def fileFormat: FileFormat = params.getFileFormat
      override def csvFileFormatParams: CsvFileFormatParams = params.getCsvFileFormatParams
    }

  // Similar code in Input/Output TODO DRY
  object InputFileStorageType {
    def get(inputStorageParams: DatasourceParams): InputStorageTypeChoice.File =
      new InputStorageTypeChoice.File()
        .setSourceFile(inputStorageParams.path)
        .setFileFormat(inputStorageParams.fileFormat match {
          case FileFormat.JSON => new InputFileFormatChoice.Json()
          case FileFormat.PARQUET => new InputFileFormatChoice.Parquet()
          case FileFormat.CSV => csvFormatChoice(inputStorageParams.csvFileFormatParams)
        })

    private def csvFormatChoice(csvParams: CsvFileFormatParams): InputFileFormatChoice.Csv =
      new InputFileFormatChoice.Csv()
        .setNamesIncluded(csvParams.getIncludeHeader)
        .setShouldConvertToBoolean(csvParams.getConvert01ToBoolean)
        .setCsvColumnSeparator(csvParams.getSeparatorType match {
          case CsvSeparatorType.COLON => ColumnSeparatorChoice.Colon()
          case CsvSeparatorType.COMMA => ColumnSeparatorChoice.Comma()
          case CsvSeparatorType.SEMICOLON => ColumnSeparatorChoice.Semicolon()
          case CsvSeparatorType.SPACE => ColumnSeparatorChoice.Space()
          case CsvSeparatorType.TAB => ColumnSeparatorChoice.Tab()
          case CsvSeparatorType.CUSTOM =>
            ColumnSeparatorChoice.Custom()
              .setCustomColumnSeparator(csvParams.getCustomSeparator)
        })
  }

  object OutputFileStorageType {
    def get(inputStorageParams: DatasourceParams): OutputStorageTypeChoice.File =
      new OutputStorageTypeChoice.File()
        .setOutputFile(inputStorageParams.path)
        .setFileFormat(inputStorageParams.fileFormat match {
          case FileFormat.JSON => new OutputFileFormatChoice.Json()
          case FileFormat.PARQUET => new OutputFileFormatChoice.Parquet()
          case FileFormat.CSV => csvFormatChoice(inputStorageParams.csvFileFormatParams)
        })

    private def csvFormatChoice(csvParams: CsvFileFormatParams): OutputFileFormatChoice.Csv =
      new OutputFileFormatChoice.Csv()
        .setNamesIncluded(csvParams.getIncludeHeader)
        .setCsvColumnSeparator(csvParams.getSeparatorType match {
          case CsvSeparatorType.COLON => ColumnSeparatorChoice.Colon()
          case CsvSeparatorType.COMMA => ColumnSeparatorChoice.Comma()
          case CsvSeparatorType.SEMICOLON => ColumnSeparatorChoice.Semicolon()
          case CsvSeparatorType.SPACE => ColumnSeparatorChoice.Space()
          case CsvSeparatorType.TAB => ColumnSeparatorChoice.Tab()
          case CsvSeparatorType.CUSTOM =>
            ColumnSeparatorChoice.Custom()
              .setCustomColumnSeparator(csvParams.getCustomSeparator)
        })
  }

}
