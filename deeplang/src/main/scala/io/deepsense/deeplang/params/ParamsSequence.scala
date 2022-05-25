package io.deepsense.deeplang.params

import java.lang.reflect.Constructor

import scala.reflect.runtime.universe._
import spray.json._
import io.deepsense.deeplang.TypeUtils
import io.deepsense.deeplang.exceptions.DeepLangException
import io.deepsense.deeplang.params.exceptions.NoArgumentConstructorRequiredException

case class ParamsSequence[T <: Params](
    override val name: String,
    override val description: Option[String])
    (implicit tag: TypeTag[T])
  extends Param[Seq[T]] {

  val parameterType = ParameterType.Multiplier

  override def valueToJson(value: Seq[T]): JsValue = {
    val cells = for (params <- value) yield params.paramValuesToJson
    JsArray(cells: _*)
  }

  private val constructor: Constructor[_] = TypeUtils.constructorForType(tag.tpe).getOrElse {
    throw NoArgumentConstructorRequiredException(tag.tpe.typeSymbol.asClass.name.decodedName.toString)
  }

  private def innerParamsInstance: T = constructor.newInstance().asInstanceOf[T]

  override def valueFromJson(jsValue: JsValue): Seq[T] = jsValue match {
    case JsArray(vector) =>
      for (innerJsValue <- vector) yield {
        innerParamsInstance.setParamsFromJson(innerJsValue)
      }
    case _ => throw new DeserializationException(s"Cannot fill parameters sequence" +
      s"with $jsValue: array expected.")
  }

  override def extraJsFields: Map[String, JsValue] = Map(
    "values" -> innerParamsInstance.paramsToJson
  )

  override def replicate(name: String): ParamsSequence[T] = copy(name = name)

  override def validate(value: Seq[T]): Vector[DeepLangException] = {
    value.flatMap(_.validateParams).toVector
  }
}
