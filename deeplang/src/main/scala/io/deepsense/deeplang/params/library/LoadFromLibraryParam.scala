package io.deepsense.deeplang.params.library

import spray.json.DefaultJsonProtocol.StringJsonFormat

import io.deepsense.deeplang.params.{ParamWithJsFormat, ParameterType}
import io.deepsense.deeplang.params.ParameterType.ParameterType

case class LoadFromLibraryParam(
    name: String,
    override val description: Option[String])
  extends ParamWithJsFormat[String] {

  override def replicate(name: String): LoadFromLibraryParam = copy(name = name)

  override val parameterType: ParameterType = ParameterType.LoadFromLibrary
}
