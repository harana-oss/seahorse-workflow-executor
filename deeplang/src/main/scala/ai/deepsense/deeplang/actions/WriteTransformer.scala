package ai.deepsense.deeplang.actions

import java.io.File
import java.io.IOException

import scala.reflect.runtime.{universe => ru}
import ai.deepsense.commons.utils.Version
import ai.deepsense.commons.utils.FileOperations.deleteRecursivelyIfExists
import ai.deepsense.deeplang.Action.Id
import ai.deepsense.deeplang.documentation.OperationDocumentation
import ai.deepsense.deeplang.actionobjects.Transformer
import ai.deepsense.deeplang.actions.exceptions.DeepSenseIOException
import ai.deepsense.deeplang.parameters.BooleanParameter
import ai.deepsense.deeplang.parameters.Params
import ai.deepsense.deeplang.parameters.StringParameter
import ai.deepsense.deeplang.Action1To0
import ai.deepsense.deeplang.ExecutionContext

import java.net.URI
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path

case class WriteTransformer() extends Action1To0[Transformer] with Params with OperationDocumentation {

  override val id: Id = "58368deb-68d0-4657-ae3f-145160cb1e2b"

  override val name: String = "Write Transformer"

  override val description: String = "Writes a Transformer to a directory"

  override val since: Version = Version(1, 1, 0)

  val shouldOverwrite = BooleanParameter(
    name = "overwrite",
    description = Some("Should an existing transformer with the same name be overwritten?")
  )

  setDefault(shouldOverwrite, true)

  def getShouldOverwrite: Boolean = $(shouldOverwrite)

  def setShouldOverwrite(value: Boolean): this.type = set(shouldOverwrite, value)

  val outputPath = StringParameter(name = "output path", description = Some("The output path for writing the Transformer."))

  def getOutputPath: String = $(outputPath)

  def setOutputPath(value: String): this.type = set(outputPath, value)

  val specificParams: Array[ai.deepsense.deeplang.parameters.Parameter[_]] = Array(outputPath, shouldOverwrite)

  override protected def execute(transformer: Transformer)(context: ExecutionContext): Unit = {
    val outputDictPath = getOutputPath
    try {
      if (getShouldOverwrite)
        removeDirectory(context, outputDictPath)
      transformer.save(context, outputDictPath)
    } catch {
      case e: IOException =>
        logger.error(s"WriteTransformer error. Could not write transformer to the directory", e)
        throw DeepSenseIOException(e)
    }
  }

  private def removeDirectory(context: ExecutionContext, path: String): Unit = {
    if (path.startsWith("hdfs://")) {
      val configuration = context.sparkContext.hadoopConfiguration
      val hdfs          = FileSystem.get(new URI(extractHdfsAddress(path)), configuration)
      hdfs.delete(new Path(path), true)
    } else
      deleteRecursivelyIfExists(new File(path))
  }

  private def extractHdfsAddress(path: String): String = {
    // first group: "hdfs://ip.addr.of.hdfs", second group: "/some/path/on/hdfs"
    val regex                 = "(hdfs:\\/\\/[^\\/]*)(.*)".r
    val regex(hdfsAddress, _) = path
    hdfsAddress
  }

  @transient
  override lazy val tTagTI_0: ru.TypeTag[Transformer] = ru.typeTag[Transformer]

}

object WriteTransformer {

  def apply(outputPath: String): WriteTransformer =
    new WriteTransformer().setOutputPath(outputPath)

}
