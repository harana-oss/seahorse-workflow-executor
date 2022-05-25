package io.deepsense.deeplang.params.choice

import io.deepsense.deeplang.exceptions.DeepLangException
import io.deepsense.deeplang.params.ParameterType
import spray.json.DefaultJsonProtocol._
import spray.json._

import scala.reflect.runtime.universe._

case class MultipleChoiceParam[T <: Choice](
    override val name: String,
    override val description: Option[String])
  (implicit tag: TypeTag[T])
  extends AbstractChoiceParam[T, Set[T]] {

  override protected def serializeDefault(choices: Set[T]): JsValue =
    JsArray(choices.toSeq.map(choice => JsString(choice.name)): _*)

  val parameterType = ParameterType.MultipleChoice

  override def valueToJson(value: Set[T]): JsValue =
    value.foldLeft(JsObject())(
      (acc: JsObject, choice: T) => JsObject(acc.fields ++ choiceToJson(choice).fields))

  protected override def valueFromJsMap(jsMap: Map[String, JsValue]): Set[T] = {
    jsMap.toList.map {
      case (label, innerJsValue) => choiceFromJson(label, innerJsValue)
    }.toSet
  }

  override def validate(value: Set[T]): Vector[DeepLangException] = {
    value.toVector.flatMap { _.validateParams }
  }

  override def replicate(name: String): MultipleChoiceParam[T] = copy(name = name)
}
