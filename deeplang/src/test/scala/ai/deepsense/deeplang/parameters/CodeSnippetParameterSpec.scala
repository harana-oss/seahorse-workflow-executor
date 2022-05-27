package ai.deepsense.deeplang.parameters

import spray.json.DefaultJsonProtocol.StringJsonFormat
import spray.json._

class CodeSnippetParameterSpec extends AbstractParameterSpec[String, CodeSnippetParameter] {

  override def className: String = "CodeSnippetParam"

  override def paramFixture: (CodeSnippetParameter, JsValue) = {
    val description = "myDescription"
    val param       = CodeSnippetParameter(
      "myName",
      Some(description),
      CodeSnippetLanguage(CodeSnippetLanguage.python)
    )
    val js          = JsObject(
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
