package ai.deepsense.deeplang.parameters.choice

import scala.reflect.runtime.universe._

import spray.json._
import ai.deepsense.deeplang.parameters.Parameter
import ai.deepsense.deeplang.parameters.exceptions.NoArgumentConstructorRequiredException
import ai.deepsense.deeplang.utils.TypeUtils
import ai.deepsense.models.json.graph.GraphJsonProtocol.GraphReader

/** @tparam T
  *   Type of choice items available to be chosen.
  * @tparam U
  *   Type of stored value. This can be type T or a collection of type T.
  */
abstract class AbstractChoiceParameter[T <: Choice, U](implicit tag: TypeTag[T]) extends Parameter[U] {

  override def extraJsFields: Map[String, JsValue] = Map(
    "values" -> JsArray(choiceInstances.map(_.toJson) _: _*)
  )

  override def valueFromJson(jsValue: JsValue, graphReader: GraphReader): U = jsValue match {
    case JsObject(map) =>
      valueFromJsMap(map, graphReader)
    case _             =>
      throw new DeserializationException(
        s"Cannot fill choice parameter with $jsValue:" +
          s" object expected."
      )
  }

  protected def valueFromJsMap(jsMap: Map[String, JsValue], graphReader: GraphReader): U

  val choiceInstances: Seq[T] = {
    val directSubclasses      = tag.tpe.typeSymbol.asClass.knownDirectSubclasses
    val instances: Set[T]     =
      for (symbol <- directSubclasses)
        yield TypeUtils
          .constructorForType(symbol.typeSignature, tag.mirror)
          .getOrElse {
            throw NoArgumentConstructorRequiredException(symbol.asClass.name.decodedName.toString)
          }
          .newInstance()
          .asInstanceOf[T]
    val allSubclassesDeclared =
      instances.forall(instance => instance.choiceOrder.contains(instance.getClass))
    require(
      allSubclassesDeclared,
      "Not all choices were declared in choiceOrder map. " +
        s"Declared: {${instances.head.choiceOrder.map(smartClassName(_)).mkString(", ")}}, " +
        s"All choices: {${instances.map(i => smartClassName(i.getClass)).mkString(", ")}}"
    )
    instances.toList.sortBy(choice => choice.choiceOrder.indexOf(choice.getClass))
  }

  private def smartClassName[T](clazz: Class[T]) = {
    val simpleName = clazz.getSimpleName
    if (simpleName == null) clazz.getName else simpleName
  }

  protected lazy val choiceInstancesByName: Map[String, T] = choiceInstances.map { case choice =>
    choice.name -> choice
  }.toMap

  protected def choiceFromJson(chosenLabel: String, jsValue: JsValue, graphReader: GraphReader): T = {
    choiceInstancesByName.get(chosenLabel) match {
      case Some(choice) => choice.setParamsFromJson(jsValue, graphReader)
      case None         =>
        throw new DeserializationException(
          s"Invalid choice $chosenLabel in " +
            s" choice parameter. Available choices: ${choiceInstancesByName.keys.mkString(",")}."
        )
    }
  }

  protected def choiceToJson(value: T): JsObject =
    JsObject(value.name -> value.paramValuesToJson)

}
