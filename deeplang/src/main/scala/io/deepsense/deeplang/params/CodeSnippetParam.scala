package io.deepsense.deeplang.params

import spray.json.DefaultJsonProtocol.StringJsonFormat
import spray.json.JsValue

import io.deepsense.deeplang.params.ParameterType.ParameterType

case class CodeSnippetParam(
    override val name: String,
    override val description: Option[String],
    language: CodeSnippetLanguage
) extends ParamWithJsFormat[String] {

  override val parameterType: ParameterType = ParameterType.CodeSnippet

  override protected def extraJsFields: Map[String, JsValue] = Map("language" -> language.toJson)

  override def replicate(name: String): CodeSnippetParam = copy(name = name)

}
