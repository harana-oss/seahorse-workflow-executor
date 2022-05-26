package io.deepsense.deeplang.params

import spray.json.DefaultJsonProtocol.StringJsonFormat
import spray.json._

class CodeSnippetParamSpec extends AbstractParamSpec[String, CodeSnippetParam] {

  override def className: String = "CodeSnippetParam"

  override def paramFixture: (CodeSnippetParam, JsValue) = {
    val description = "myDescription"
    val param = CodeSnippetParam(
      "myName",
      Some(description),
      CodeSnippetLanguage(CodeSnippetLanguage.python)
    )
    val js = JsObject(
      "type"        -> "codeSnippet".toJson,
      "name"        -> param.name.toJson,
      "description" -> description.toJson,
      "language"    -> JsObject("name" -> "python".toJson),
      "isGriddable" -> JsFalse,
      "default"     -> JsNull
    )
    (param, js)
  }

  override def valueFixture: (String, JsValue) = ("some python code", "some python code".toJson)

}
