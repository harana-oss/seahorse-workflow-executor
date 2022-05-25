package io.deepsense.deeplang.doperations.readwritedataframe.filestorage

import org.apache.spark.SparkException
import io.deepsense.commons.utils.LoggerForCallerClass
import io.deepsense.deeplang.doperables.dataframe.DataFrame
import io.deepsense.deeplang.doperations.exceptions.WriteFileException
import io.deepsense.deeplang.doperations.inout.OutputFileFormatChoice.Csv
import io.deepsense.deeplang.doperations.inout.OutputStorageTypeChoice
import io.deepsense.deeplang.doperations.readwritedataframe.{FilePath, FilePathFromLibraryPath, FileScheme}
import io.deepsense.deeplang.doperations.readwritedataframe.filestorage.csv.CsvSchemaStringifierBeforeCsvWriting
import io.deepsense.deeplang.exceptions.DeepLangException
import io.deepsense.deeplang.{ExecutionContext, FileSystemClient}
import org.apache.spark.sql.SaveMode

object DataFrameToFileWriter {

  val logger = LoggerForCallerClass()

  def writeToFile(
      fileChoice: OutputStorageTypeChoice.File,
      context: ExecutionContext,
      dataFrame: DataFrame): Unit = {
    implicit val ctx = context

    val path = FileSystemClient.replaceLeadingTildeWithHomeDirectory(fileChoice.getOutputFile())
    val filePath = FilePath(path)
    val saveMode = if (fileChoice.getShouldOverwrite) SaveMode.Overwrite else SaveMode.ErrorIfExists

    try {
      val preprocessed = fileChoice.getFileFormat() match {
        case csv: Csv => CsvSchemaStringifierBeforeCsvWriting.preprocess(dataFrame)
        case other => dataFrame
      }
      writeUsingProvidedFileScheme(fileChoice, preprocessed, filePath, saveMode)
    } catch {
      case e: SparkException =>
        logger.error(s"WriteDataFrame error: Spark problem. Unable to write file to $path", e)
        throw WriteFileException(path, e)
    }
  }

  private def writeUsingProvidedFileScheme(
      fileChoice: OutputStorageTypeChoice.File, dataFrame: DataFrame, path: FilePath, saveMode: SaveMode
    )(implicit context: ExecutionContext): Unit = {
    import FileScheme._
    path.fileScheme match {
      case Library =>
        val filePath = FilePathFromLibraryPath(path)
        val FilePath(_, libraryPath) = filePath
        new java.io.File(libraryPath).getParentFile.mkdirs()
        writeUsingProvidedFileScheme(fileChoice, dataFrame, filePath, saveMode)
      case FileScheme.File => DriverFiles.write(dataFrame, path, fileChoice.getFileFormat(), saveMode)
      case HDFS => ClusterFiles.write(dataFrame, path, fileChoice.getFileFormat(), saveMode)
      case HTTP | HTTPS | FTP => throw NotSupportedScheme(path.fileScheme)
    }
  }

  case class NotSupportedScheme(fileScheme: FileScheme)
    extends DeepLangException(s"Not supported file scheme ${fileScheme.pathPrefix}")

}
