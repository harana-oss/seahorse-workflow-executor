package ai.deepsense.deeplang

import org.scalatest.BeforeAndAfter
import org.scalatest.BeforeAndAfterAll
import ai.deepsense.deeplang.actions.inout.InputFileFormatChoice
import ai.deepsense.deeplang.actions.readwritedataframe.FilePath
import ai.deepsense.deeplang.actions.readwritedataframe.FileScheme
import ai.deepsense.deeplang.filesystemclients.LocalFileSystemClient

trait TestFiles { self: BeforeAndAfter with BeforeAndAfterAll =>

  private def fileSystemClient = LocalFileSystemClient()

  before {
    fileSystemClient.delete(testsDir)
    new java.io.File(testsDir + "/id").getParentFile.mkdirs()
    fileSystemClient.copyLocalFile(getClass.getResource("/test_files/").getPath, testsDir)
  }

  after {
    fileSystemClient.delete(testsDir)
  }

  def testFile(fileFormat: InputFileFormatChoice, fileScheme: FileScheme): String = {
    val format   = fileFormat.getClass.getSimpleName.toLowerCase()
    val fileName = s"some_$format.$format"
    val path     = fileScheme match {
      case FileScheme.HTTPS => "https://s3.amazonaws.com/workflowexecutor/test_data/"
      case FileScheme.File  => absoluteTestsDirPath.fullPath
      case other            => throw new IllegalStateException(s"$other not supported")
    }
    val fullPath = path + fileName
    fullPath
  }

  def someCsvFile = FilePath(FileScheme.File, testsDir + "/some_csv.csv")

  private val testsDir = "target/tests"

  def absoluteTestsDirPath: FilePath = FilePath(FileScheme.File, rawAbsoluteTestsDirPath)

  private def rawAbsoluteTestsDirPath = new java.io.File(testsDir).getAbsoluteFile.toString + "/"

}
