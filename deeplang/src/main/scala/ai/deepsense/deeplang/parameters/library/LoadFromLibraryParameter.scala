package ai.deepsense.deeplang.parameters.library

import spray.json.DefaultJsonProtocol.StringJsonFormat

import ai.deepsense.deeplang.parameters.ParameterWithJsFormat
import ai.deepsense.deeplang.parameters.ParameterType
import ai.deepsense.deeplang.parameters.ParameterType.ParameterType

case class LoadFromLibraryParameter(name: String, override val description: Option[String])
    extends ParameterWithJsFormat[String] {

  override def replicate(name: String): LoadFromLibraryParameter = copy(name = name)

  override val parameterType: ParameterType = ParameterType.LoadFromLibrary

}
