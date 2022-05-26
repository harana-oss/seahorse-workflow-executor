package ai.deepsense.deeplang.params.library

import spray.json.DefaultJsonProtocol.StringJsonFormat

import ai.deepsense.deeplang.params.ParameterType.ParameterType
import ai.deepsense.deeplang.params.ParamWithJsFormat
import ai.deepsense.deeplang.params.ParameterType

case class SaveToLibraryParam(override val name: String, override val description: Option[String])
    extends ParamWithJsFormat[String] {

  override def replicate(name: String): SaveToLibraryParam = copy(name = name)

  override val parameterType: ParameterType = ParameterType.SaveToLibrary

}
