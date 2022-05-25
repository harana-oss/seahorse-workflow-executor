package io.deepsense.commons.mail.templates.instances

import java.io.File

import scala.util.Try

import org.apache.commons.io.FilenameUtils
import org.fusesource.scalate.{TemplateEngine, Template => VendorTemplate}

import io.deepsense.commons.mail.templates.TemplateLoadedFromFile
import io.deepsense.commons.utils.DirectoryListFileFinder

class ScalateTemplate(val scalateTemplateConfig: ScalateTemplateConfig)
  extends TemplateLoadedFromFile[VendorTemplate] {

  def this() = this(ScalateTemplateConfig())

  object ScalateTemplateFinder extends DirectoryListFileFinder(scalateTemplateConfig.templatesDirs) {

    override def filePredicate(f: File, desc: Option[String]): Boolean = {
      require(desc.isDefined, "No template name given")
      val filename = f.getName
      filename.startsWith(desc.get) &&
        ScalateTemplate.scalateExtensions.contains(FilenameUtils.getExtension(filename))
    }

  }

  override def loadTemplateFromFile(f: File): Try[VendorTemplate] = Try {
    ScalateTemplate.engine.load(f)
  }

  override def renderTemplate(template: VendorTemplate, attributes: Map[String, Any]): String = {
    ScalateTemplate.engine.layout("", template, attributes)
  }

  override def resolveTemplateName(templateName: String): Try[File] =
    ScalateTemplateFinder.findFile(templateName)
}

object ScalateTemplate {

  def apply(scalateTemplateConfig: ScalateTemplateConfig): ScalateTemplate =
    new ScalateTemplate(scalateTemplateConfig)
  def apply(): ScalateTemplate = new ScalateTemplate()

  val engine = new TemplateEngine()

  val scalateExtensions = Set("scaml", "haml", "ssp", "jade", "mustache")

}
