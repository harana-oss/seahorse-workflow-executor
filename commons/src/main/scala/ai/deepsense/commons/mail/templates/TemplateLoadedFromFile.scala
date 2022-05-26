package ai.deepsense.commons.mail.templates

import java.io.File

import scala.util.Try

/** This class represents the templates that are loaded from File.
  *
  * @tparam T
  *   template class
  */
trait TemplateLoadedFromFile[T] extends Template[T] {

  def resolveTemplateName(templateName: String): Try[File]

  def loadTemplateFromFile(file: File): Try[T]

  override def loadTemplate(templateName: String): Try[T] =
    resolveTemplateName(templateName)
      .flatMap(loadTemplateFromFile)

}
