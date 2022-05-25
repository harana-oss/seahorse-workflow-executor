package io.deepsense.deeplang.params.choice

import scala.reflect.runtime.universe._

import spray.json._

import io.deepsense.deeplang.TypeUtils
import io.deepsense.deeplang.params.Param
import io.deepsense.deeplang.params.exceptions.NoArgumentConstructorRequiredException

/**
 * @tparam T Type of choice items available to be chosen.
 * @tparam U Type of stored value. This can be type T or a collection of type T.
 */
abstract class AbstractChoiceParam[T <: Choice, U](implicit tag: TypeTag[T]) extends Param[U] {

  override def extraJsFields: Map[String, JsValue] = Map(
    "values" -> JsArray(choiceInstances.map(_.toJson): _*)
  )

  override def valueFromJson(jsValue: JsValue): U = jsValue match {
    case JsObject(map) =>
      valueFromJsMap(map)
    case _ => throw new DeserializationException(s"Cannot fill choice parameter with $jsValue:" +
      s" object expected.")
  }

  protected def valueFromJsMap(jsMap: Map[String, JsValue]): U

  val choiceInstances: Seq[T] = {
    val directSubclasses = tag.tpe.typeSymbol.asClass.knownDirectSubclasses
    val instances: Set[T] = for (symbol <- directSubclasses)
      yield TypeUtils.constructorForType(symbol.typeSignature).getOrElse {
        throw NoArgumentConstructorRequiredException(symbol.asClass.name.decodedName.toString)
      }.newInstance().asInstanceOf[T]
    val allSubclassesDeclared =
      instances.forall(instance => instance.choiceOrder.contains(instance.getClass))
    require(allSubclassesDeclared,
      "Not all choices were declared in choiceOrder map. " +
      s"Declared: {${instances.head.choiceOrder.map(smartClassName(_)).mkString(", ")}}, " +
      s"All choices: {${instances.map(i => smartClassName(i.getClass)).mkString(", ")}}")
    instances.toList.sortBy(choice => choice.choiceOrder.indexOf(choice.getClass))
  }

  private def smartClassName[T](clazz: Class[T]) = {
    val simpleName = clazz.getSimpleName
    if (simpleName == null) clazz.getName else simpleName
  }

  protected lazy val choiceInstancesByName: Map[String, T] = choiceInstances.map {
    case choice => choice.name -> choice
  }.toMap

  protected def choiceFromJson(chosenLabel: String, jsValue: JsValue): T = {
    choiceInstancesByName.get(chosenLabel) match {
      case Some(choice) => choice.setParamsFromJson(jsValue)
      case None => throw new DeserializationException(s"Invalid choice $chosenLabel in " +
        s" choice parameter. Available choices: ${choiceInstancesByName.keys.mkString(",")}.")
    }
  }

  protected def choiceToJson(value: T): JsObject = JsObject(value.name -> value.paramValuesToJson)
}
