package ai.deepsense.deeplang.parameters

import spray.json.DefaultJsonProtocol.BooleanJsonFormat

case class BooleanParameter(override val name: String, override val description: Option[String])
    extends ParameterWithJsFormat[Boolean] {

  override val parameterType = ParameterType.Boolean

  override def replicate(name: String): BooleanParameter = copy(name = name)

}
