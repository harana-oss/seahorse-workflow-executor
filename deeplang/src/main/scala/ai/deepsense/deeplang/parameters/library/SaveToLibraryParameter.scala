package ai.deepsense.deeplang.parameters.library

import spray.json.DefaultJsonProtocol.StringJsonFormat

import ai.deepsense.deeplang.parameters.ParameterType.ParameterType
import ai.deepsense.deeplang.parameters.ParameterWithJsFormat
import ai.deepsense.deeplang.parameters.ParameterType

case class SaveToLibraryParameter(override val name: String, override val description: Option[String])
    extends ParameterWithJsFormat[String] {

  override def replicate(name: String): SaveToLibraryParameter = copy(name = name)

  override val parameterType: ParameterType = ParameterType.SaveToLibrary

}
