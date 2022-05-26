package io.deepsense.workflowexecutor.pyspark

import scala.collection.JavaConverters._

import com.typesafe.config.ConfigFactory

class PythonPathGenerator(
    private val pySparkPath: String
) {

  private val config = ConfigFactory.load.getConfig("pyspark")

  private val additionalPythonPath: Seq[String] = config.getStringList("python-path").asScala

  private val additionalPaths = additionalPythonPath.map(p => s"$pySparkPath/$p")

  private val pythonPathEnvKey = "PYTHONPATH"

  private val envPythonPath = Option(System.getenv().get(pythonPathEnvKey))

  val generatedPythonPath: Seq[String] = pySparkPath +: (additionalPaths ++ envPythonPath)

  def pythonPath(additionalPaths: String*): String =
    (generatedPythonPath ++ additionalPaths).mkString(":")

  def env(additionalPaths: String*): (String, String) =
    (pythonPathEnvKey, pythonPath(additionalPaths: _*))

}
