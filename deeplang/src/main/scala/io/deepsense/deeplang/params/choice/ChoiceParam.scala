package io.deepsense.deeplang.params.choice

import scala.reflect.runtime.universe._

import spray.json._

import io.deepsense.deeplang.exceptions.DeepLangException
import io.deepsense.deeplang.params.ParameterType

class ChoiceParam[T <: Choice](
    override val name: String,
    override val description: Option[String])
    (implicit tag: TypeTag[T])
  extends AbstractChoiceParam[T, T] {

  override protected def serializeDefault(choice: T): JsValue = JsString(choice.name)

  val parameterType = ParameterType.Choice

  override def valueToJson(value: T): JsValue = choiceToJson(value)

  protected override def valueFromJsMap(jsMap: Map[String, JsValue]): T = {
    if (jsMap.size != 1) {
      throw new DeserializationException(s"There should be only one selected option in choice" +
        s" parameter, but there are ${jsMap.size} in ${jsMap.toString}.")
    }
    val (label, innerJsValue) = jsMap.iterator.next()
    choiceFromJson(label, innerJsValue)
  }

  override def validate(value: T): Vector[DeepLangException] = {
    value.validateParams
  }

  override def replicate(name: String): ChoiceParam[T] =
    new ChoiceParam[T](name, description)
}

object ChoiceParam {
  def apply[T <: Choice : TypeTag](
      name: String,
      description: Option[String]): ChoiceParam[T] =
    new ChoiceParam[T](name, description)
}
