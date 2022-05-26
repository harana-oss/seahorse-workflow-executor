package io.deepsense.deeplang.doperations.readwritedataframe.filestorage

import org.apache.spark.sql._

import io.deepsense.deeplang.ExecutionContext
import io.deepsense.deeplang.doperations.inout.InputFileFormatChoice.Csv
import io.deepsense.deeplang.doperations.inout.InputFileFormatChoice
import io.deepsense.deeplang.doperations.inout.InputStorageTypeChoice
import io.deepsense.deeplang.doperations.readwritedataframe._
import io.deepsense.deeplang.doperations.readwritedataframe.filestorage.csv.CsvSchemaInferencerAfterReading

object DataFrameFromFileReader {

  def readFromFile(fileChoice: InputStorageTypeChoice.File)(implicit context: ExecutionContext): DataFrame = {
    val path         = FilePath(fileChoice.getSourceFile)
    val rawDataFrame = readUsingProvidedFileScheme(path, fileChoice.getFileFormat)
    val postprocessed = fileChoice.getFileFormat match {
      case csv: Csv => CsvSchemaInferencerAfterReading.postprocess(csv)(rawDataFrame)
      case other    => rawDataFrame
    }
    postprocessed
  }

  private def readUsingProvidedFileScheme(path: FilePath, fileFormat: InputFileFormatChoice)(implicit
      context: ExecutionContext
  ): DataFrame =
    path.fileScheme match {
      case FileScheme.Library =>
        val filePath = FilePathFromLibraryPath(path)
        readUsingProvidedFileScheme(filePath, fileFormat)
      case FileScheme.File => DriverFiles.read(path.pathWithoutScheme, fileFormat)
      case FileScheme.HTTP | FileScheme.HTTPS | FileScheme.FTP =>
        val downloadedPath = FileDownloader.downloadFile(path.fullPath)
        readUsingProvidedFileScheme(downloadedPath, fileFormat)
      case FileScheme.HDFS => ClusterFiles.read(path, fileFormat)
    }

}
