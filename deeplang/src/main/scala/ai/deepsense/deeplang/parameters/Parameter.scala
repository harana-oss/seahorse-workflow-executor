package ai.deepsense.deeplang.parameters

import java.util.Objects

import spray.json.DefaultJsonProtocol._
import spray.json._

import ai.deepsense.deeplang.exceptions.FlowException
import ai.deepsense.deeplang.parameters.ParameterType._
import ai.deepsense.models.json.graph.GraphJsonProtocol.GraphReader

abstract class Parameter[T] extends java.io.Serializable {

  val name: String

  val description: Option[String]

  def constraints: String = ""

  val parameterType: ParameterType

  def validate(value: T): Vector[FlowException] = Vector.empty

  val isGriddable: Boolean = false

  /** Used to extract public parameters in custom transformer.
    *
    * @param name
    *   name of replicated parameter
    * @return
    *   replicated parameter
    */
  def replicate(name: String): Parameter[T]

  /** Describes json representation of this parameter.
    * @param maybeDefault
    *   Optional default value of parameter. Should be of type Option[T], but we need to receive Any because Params have
    *   to use this method without knowing T.
    */
  final def toJson(maybeDefault: Option[Any]): JsObject = {
    val basicFields = Map(
      "name"        -> name.toJson,
      "type"        -> parameterType.toString.toJson, // TODO json format for parameterType
      "description" -> (description.getOrElse("") + constraints).toJson,
      "isGriddable" -> isGriddable.toJson,
      "default"     -> maybeDefault.map(default => serializeDefault(default.asInstanceOf[T])).getOrElse(JsNull)
    )
    JsObject(basicFields ++ extraJsFields)
  }

  /** Describes default serialization of default values.
    * @param default
    *   Default value of parameter
    */
  protected def serializeDefault(default: T): JsValue =
    valueToJson(default)

  /** Subclasses should overwrite this method if they want to add custom fields to json description. */
  protected def extraJsFields: Map[String, JsValue] = Map.empty

  // scalastyle:off
  def ->(value: T): ParamPair[T] = ParamPair(this, value)
  // scalastyle:on

  override def toString: String = s"Param($parameterType, $name)"

  def valueToJson(value: T): JsValue

  /** Helper method for Params, which don't know T. */
  private[parameters] def anyValueToJson(value: Any): JsValue =
    valueToJson(value.asInstanceOf[T])

  def valueFromJson(jsValue: JsValue, graphReader: GraphReader): T

  def canEqual(other: Any): Boolean = other.isInstanceOf[Parameter[T]]

  override def equals(other: Any): Boolean = other match {
    case that: Parameter[T] =>
      (that.canEqual(this)) &&
      name == that.name &&
      description == that.description &&
      parameterType == that.parameterType
    case _              => false
  }

  override def hashCode(): Int =
    Objects.hash(name, description, parameterType)

}
