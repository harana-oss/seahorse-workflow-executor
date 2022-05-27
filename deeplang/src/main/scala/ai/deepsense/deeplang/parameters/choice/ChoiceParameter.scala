package ai.deepsense.deeplang.parameters.choice

import scala.reflect.runtime.universe._

import spray.json._

import ai.deepsense.deeplang.exceptions.FlowException
import ai.deepsense.deeplang.parameters.ParameterType
import ai.deepsense.models.json.graph.GraphJsonProtocol.GraphReader

class ChoiceParameter[T <: Choice](override val name: String, override val description: Option[String])(implicit
                                                                                                        tag: TypeTag[T]
) extends AbstractChoiceParameter[T, T] {

  override protected def serializeDefault(choice: T): JsValue = JsString(choice.name)

  val parameterType = ParameterType.Choice

  override def valueToJson(value: T): JsValue = choiceToJson(value)

  override protected def valueFromJsMap(jsMap: Map[String, JsValue], graphReader: GraphReader): T = {
    if (jsMap.size != 1)
      throw new DeserializationException(
        s"There should be only one selected option in choice" +
          s" parameter, but there are ${jsMap.size} in ${jsMap.toString}."
      )
    val (label, innerJsValue) = jsMap.iterator.next()
    choiceFromJson(label, innerJsValue, graphReader)
  }

  override def validate(value: T): Vector[FlowException] =
    value.validateParams

  override def replicate(name: String): ChoiceParameter[T] =
    new ChoiceParameter[T](name, description)

}

object ChoiceParameter {

  def apply[T <: Choice: TypeTag](name: String, description: Option[String]): ChoiceParameter[T] =
    new ChoiceParameter[T](name, description)

}
