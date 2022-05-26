package io.deepsense.deeplang.doperations.readwritedataframe.validators

import io.deepsense.deeplang.doperations.inout.InputFileFormatChoice
import io.deepsense.deeplang.doperations.inout.InputStorageTypeChoice
import io.deepsense.deeplang.doperations.inout.OutputFileFormatChoice
import io.deepsense.deeplang.doperations.inout.OutputStorageTypeChoice
import io.deepsense.deeplang.doperations.readwritedataframe.filestorage.ParquetNotSupported
import io.deepsense.deeplang.doperations.readwritedataframe.FilePath
import io.deepsense.deeplang.doperations.readwritedataframe.FileScheme
import io.deepsense.deeplang.doperations.ReadDataFrame
import io.deepsense.deeplang.doperations.WriteDataFrame

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
          case _ =>
        }
      case _ =>
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
          case _ =>
        }
      case _ =>
    }
  }

}
