package ai.deepsense.deeplang.doperations.readwritedataframe.validators

import ai.deepsense.deeplang.doperations.inout.InputFileFormatChoice
import ai.deepsense.deeplang.doperations.inout.InputStorageTypeChoice
import ai.deepsense.deeplang.doperations.inout.OutputFileFormatChoice
import ai.deepsense.deeplang.doperations.inout.OutputStorageTypeChoice
import ai.deepsense.deeplang.doperations.readwritedataframe.filestorage.ParquetNotSupported
import ai.deepsense.deeplang.doperations.readwritedataframe.FilePath
import ai.deepsense.deeplang.doperations.readwritedataframe.FileScheme
import ai.deepsense.deeplang.doperations.ReadDataFrame
import ai.deepsense.deeplang.doperations.WriteDataFrame

object ParquetSupportedOnClusterOnly {

  def validate(wdf: WriteDataFrame): Unit = {
    import OutputFileFormatChoice._
    import OutputStorageTypeChoice._

    wdf.getStorageType() match {
      case file: File =>
        file.getFileFormat() match {
          case _: Parquet =>
            val path       = file.getOutputFile()
            val filePath   = FilePath(path)
            val fileScheme = filePath.fileScheme
            if (!FileScheme.supportedByParquet.contains(fileScheme))
              throw ParquetNotSupported
          case _          =>
        }
      case _          =>
    }
  }

  def validate(rdf: ReadDataFrame): Unit = {
    import InputFileFormatChoice._
    import InputStorageTypeChoice._

    rdf.getStorageType() match {
      case file: File =>
        file.getFileFormat() match {
          case _: Parquet =>
            val path       = file.getSourceFile()
            val filePath   = FilePath(path)
            val fileScheme = filePath.fileScheme
            if (!FileScheme.supportedByParquet.contains(fileScheme))
              throw ParquetNotSupported
          case _          =>
        }
      case _          =>
    }
  }

}
