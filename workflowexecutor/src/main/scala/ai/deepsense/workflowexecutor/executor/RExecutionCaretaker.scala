package ai.deepsense.workflowexecutor.executor

import org.apache.spark.api.r.SparkRBackend

import ai.deepsense.commons.utils.Logging
import ai.deepsense.deeplang.CustomCodeExecutor
import ai.deepsense.workflowexecutor.Unzip
import ai.deepsense.workflowexecutor.customcode.CustomCodeEntryPoint

class RExecutionCaretaker(rExecutorPath: String, customCodeEntryPoint: CustomCodeEntryPoint) extends Logging {

  private val backend = new SparkRBackend()

  def backendListeningPort: Int = backend.port

  def rCodeExecutor: CustomCodeExecutor =
    new RExecutor(backend.port, backend.entryPointId, customCodeEntryPoint, extractRExecutor())

  def start(): Unit = backend.start(customCodeEntryPoint)

  private def extractRExecutor(): String = {
    if (rExecutorPath.endsWith(".jar")) {
      val tempDir = Unzip.unzipToTmp(rExecutorPath, _.equals("r_executor.R"))
      s"$tempDir/r_executor.R"
    } else
      rExecutorPath
  }

}
