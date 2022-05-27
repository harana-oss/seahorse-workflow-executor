package ai.deepsense.deeplang.parameters.choice

import ai.deepsense.deeplang.exceptions.FlowException
import ai.deepsense.deeplang.parameters.ParameterType
import spray.json.DefaultJsonProtocol._
import spray.json._
import scala.reflect.runtime.universe._

import ai.deepsense.models.json.graph.GraphJsonProtocol.GraphReader

case class MultipleChoiceParameter[T <: Choice](override val name: String, override val description: Option[String])(
    implicit tag: TypeTag[T]
) extends AbstractChoiceParameter[T, Set[T]] {

  override protected def serializeDefault(choices: Set[T]): JsValue =
    JsArray(choices.toSeq.map(choice => JsString(choice.name)): _*)

  val parameterType = ParameterType.MultipleChoice

  override def valueToJson(value: Set[T]): JsValue =
    value.foldLeft(JsObject())((acc: JsObject, choice: T) => JsObject(acc.fields ++ choiceToJson(choice).fields))

  override protected def valueFromJsMap(jsMap: Map[String, JsValue], graphReader: GraphReader): Set[T] =
    jsMap.toList.map { case (label, innerJsValue) => choiceFromJson(label, innerJsValue, graphReader) }.toSet

  override def validate(value: Set[T]): Vector[FlowException] =
    value.toVector.flatMap(_.validateParams)

  override def replicate(name: String): MultipleChoiceParameter[T] = copy(name = name)

}
