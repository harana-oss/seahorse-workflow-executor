package ai.deepsense.workflowexecutor.pyspark

import scala.reflect.io.Path

import ai.deepsense.commons.utils.Logging

object Loader extends Logging {

  def load: Option[String] = Option(System.getenv("SPARK_HOME")).map(pysparkPath)

  private def pysparkPath(sparkHome: String): String = {
    val path = Path(sparkHome)./("python").toAbsolute.toString()
    logger.info("Found PySpark at: {}", path)
    path
  }

}
