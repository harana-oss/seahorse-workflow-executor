package ai.deepsense.workflowexecutor.executor

import java.net.URLEncoder

import scala.sys.process._

import ai.deepsense.commons.utils.Logging
import ai.deepsense.deeplang.CustomCodeExecutor
import ai.deepsense.workflowexecutor.customcode.CustomCodeEntryPoint

case class RExecutor(
    rBackendPort: Int,
    entryPointId: String,
    customCodeEntryPoint: CustomCodeEntryPoint,
    rExecutorScript: String
) extends CustomCodeExecutor
    with Logging {

  def isValid(code: String): Boolean = true

  val RExecutable = "Rscript"

  def run(workflowId: String, nodeId: String, code: String): Unit = {
    val command = s"""$RExecutable $rExecutorScript """ +
      s""" $rBackendPort """ +
      s""" $workflowId """ +
      s""" $nodeId """ +
      s""" $entryPointId """ +
      s""" ${URLEncoder.encode(code, "UTF-8").replace("+", "%20")} """

    logger.info(s"Starting a new RExecutor process: $command")

    val log = new StringBuffer()
    def logMethod(logmethod: String => Unit)(s: String): Unit = {
      log.append(s)
      logmethod(s)
    }

    val rLogger   = ProcessLogger(fout = logMethod(logger.debug), ferr = logMethod(logger.error))
    val errorCode = command ! rLogger
    // if RScript is successful then r_executor.R should take care of setting execution status
    if (errorCode != 0)
      customCodeEntryPoint.executionFailed(workflowId, nodeId, log.toString())
  }

}
