package ai.deepsense.deeplang.parameters

import spray.json.DefaultJsonProtocol._

case class MultipleColumnCreatorParameter(override val name: String, override val description: Option[String])
    extends ParameterWithJsFormat[Array[String]] {

  val parameterType = ParameterType.MultipleColumnCreator

  override def replicate(name: String): MultipleColumnCreatorParameter = copy(name = name)

}
