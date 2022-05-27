package ai.deepsense.deeplang.actions.readwritedataframe.filestorage

import java.io.BufferedWriter
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStreamWriter
import java.nio.file.Files
import java.nio.file.Paths
import java.util.UUID

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path

import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.actions.exceptions.HaranaIOException
import ai.deepsense.deeplang.actions.readwritedataframe.FilePath

private[filestorage] object FileDownloader {

  def downloadFile(url: String)(implicit context: ExecutionContext): FilePath = {
    if (context.tempPath.startsWith("hdfs://"))
      downloadFileToHdfs(url)
    else
      downloadFileToDriver(url)
  }

  private def downloadFileToHdfs(url: String)(implicit context: ExecutionContext) = {
    val content  = scala.io.Source.fromURL(url).getLines()
    val hdfsPath = s"${context.tempPath}/${UUID.randomUUID()}"

    val configuration = new Configuration()
    val hdfs          = FileSystem.get(configuration)
    val file          = new Path(hdfsPath)
    val hdfsStream    = hdfs.create(file)
    val writer        = new BufferedWriter(new OutputStreamWriter(hdfsStream))
    try {
      content.foreach { s =>
        writer.write(s)
        writer.newLine()
      }
    } finally {
      safeClose(writer)
      hdfs.close()
    }

    FilePath(hdfsPath)
  }

  private def downloadFileToDriver(url: String)(implicit context: ExecutionContext) = {
    val outputDirPath = Paths.get(context.tempPath)
    // We're checking if the output is a directory following symlinks.
    // The default behaviour of createDirectories is NOT to follow symlinks
    if (!Files.isDirectory(outputDirPath))
      Files.createDirectories(outputDirPath)

    val outFilePath            = Files.createTempFile(outputDirPath, "download", ".csv")
    // content is a stream. Do not invoke stuff like .toList() on it.
    val content                = scala.io.Source.fromURL(url).getLines()
    val writer: BufferedWriter =
      new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFilePath.toFile)))
    try {
      content.foreach { s =>
        writer.write(s)
        writer.newLine()
      }
    } finally
      safeClose(writer)
    FilePath(s"file:///$outFilePath")
  }

  private def safeClose(bufferedWriter: BufferedWriter): Unit = {
    try {
      bufferedWriter.flush()
      bufferedWriter.close()
    } catch {
      case e: IOException => throw new HaranaIOException(e)
    }
  }

}
