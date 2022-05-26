package ai.deepsense.deeplang

import java.io.File

import com.typesafe.config.ConfigFactory

object Config {

  def jarsDir = new File(ConfigFactory.load("deeplang.conf").getString("spark-resources-jars-dir"))

}
