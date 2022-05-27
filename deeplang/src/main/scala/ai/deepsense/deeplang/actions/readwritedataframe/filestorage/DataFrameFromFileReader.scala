package ai.deepsense.deeplang.actions.readwritedataframe.filestorage

import org.apache.spark.sql._

import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.actions.inout.InputFileFormatChoice.Csv
import ai.deepsense.deeplang.actions.inout.InputFileFormatChoice
import ai.deepsense.deeplang.actions.inout.InputStorageTypeChoice
import ai.deepsense.deeplang.actions.readwritedataframe._
import ai.deepsense.deeplang.actions.readwritedataframe.filestorage.csv.CsvSchemaInferencerAfterReading

object DataFrameFromFileReader {

  def readFromFile(fileChoice: InputStorageTypeChoice.File)(implicit context: ExecutionContext): DataFrame = {
    val path          = FilePath(fileChoice.getSourceFile)
    val rawDataFrame  = readUsingProvidedFileScheme(path, fileChoice.getFileFormat)
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
      case FileScheme.Library                                  =>
        val filePath = FilePathFromLibraryPath(path)
        readUsingProvidedFileScheme(filePath, fileFormat)
      case FileScheme.File                                     => DriverFiles.read(path.pathWithoutScheme, fileFormat)
      case FileScheme.HTTP | FileScheme.HTTPS | FileScheme.FTP =>
        val downloadedPath = FileDownloader.downloadFile(path.fullPath)
        readUsingProvidedFileScheme(downloadedPath, fileFormat)
      case FileScheme.HDFS                                     => ClusterFiles.read(path, fileFormat)
    }

}
