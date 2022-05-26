package io.deepsense.commons.mail.templates

import scala.util.Try

/** Type class representing a template
  *
  * The template class must be able to be loaded by name and must be able to be converted to String when it is provided
  * a context
  *
  * @tparam T
  *   template class
  */
trait Template[T] {

  type TemplateContext = Map[String, Any]

  def loadTemplate(templateName: String): Try[T]

  def renderTemplate(template: T, templateContext: TemplateContext): String

}
