package io.deepsense.deeplang.params

import spray.json.DefaultJsonProtocol._

case class MultipleColumnCreatorParam(
    override val name: String,
    override val description: Option[String])
  extends ParamWithJsFormat[Array[String]] {

  val parameterType = ParameterType.MultipleColumnCreator

  override def replicate(name: String): MultipleColumnCreatorParam = copy(name = name)
}
