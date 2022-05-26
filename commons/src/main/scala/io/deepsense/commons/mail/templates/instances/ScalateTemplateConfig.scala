package io.deepsense.commons.mail.templates.instances

import java.io.File

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory

class ScalateTemplateConfig(val masterConfig: Config) {

  import ScalateTemplateConfig._
  import io.deepsense.commons.utils.ConfigWithDirListsImplicits._

  def this() = this(ConfigFactory.load())

  val templatesDirs: Seq[File] = masterConfig.getConfig(scalateTemplateSubConfigPath).getDirList("templates-dirs")

}

object ScalateTemplateConfig {

  def apply(masterConfig: Config): ScalateTemplateConfig = new ScalateTemplateConfig(masterConfig)

  def apply(): ScalateTemplateConfig = new ScalateTemplateConfig()

  val scalateTemplateSubConfigPath = "scalate-templates"

}
