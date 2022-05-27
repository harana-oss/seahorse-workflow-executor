package ai.deepsense.deeplang.parameters

import spray.json._

/** Represents language of code snippet (it could be used for syntax validation and syntax highlighting in frontend). */
@SerialVersionUID(1)
case class CodeSnippetLanguage(language: CodeSnippetLanguage.CodeSnippetLanguage) {

  final def toJson: JsObject = {
    import spray.json.DefaultJsonProtocol._
    JsObject("name" -> language.toString.toJson)
  }

}

object CodeSnippetLanguage extends Enumeration {

  type CodeSnippetLanguage = Value

  val python = Value("python")

  val sql = Value("sql")

  val r = Value("r")

}
