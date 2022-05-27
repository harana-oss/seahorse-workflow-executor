package ai.deepsense.deeplang.parameters

import spray.json.DefaultJsonProtocol.StringJsonFormat
import spray.json.JsValue

import ai.deepsense.deeplang.parameters.ParameterType.ParameterType

case class CodeSnippetParameter(
    override val name: String,
    override val description: Option[String],
    language: CodeSnippetLanguage
) extends ParameterWithJsFormat[String] {

  override val parameterType: ParameterType = ParameterType.CodeSnippet

  override protected def extraJsFields: Map[String, JsValue] = Map("language" -> language.toJson)

  override def replicate(name: String): CodeSnippetParameter = copy(name = name)

}
