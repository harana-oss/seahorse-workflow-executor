package io.deepsense.deeplang.params

import spray.json.DefaultJsonProtocol.BooleanJsonFormat

case class BooleanParam(
    override val name: String,
    override val description: Option[String])
  extends ParamWithJsFormat[Boolean] {

  override val parameterType = ParameterType.Boolean

  override def replicate(name: String): BooleanParam = copy(name = name)
}
