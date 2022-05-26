package io.deepsense.workflowexecutor.pyspark

import scala.reflect.io.Path

import io.deepsense.commons.utils.Logging

class Loader(
    private val localPath: Option[String] = None
) extends Logging {

  def load: Option[String] =
    localPath match {
      case Some(x) => Some(x)
      case None =>
        Option(System.getenv("SPARK_HOME")).map(pysparkPath)
    }

  private def pysparkPath(sparkHome: String): String = {
    val path = Path(sparkHome)./("python").toAbsolute.toString()
    logger.info("Found PySpark at: {}", path)
    path
  }

}
