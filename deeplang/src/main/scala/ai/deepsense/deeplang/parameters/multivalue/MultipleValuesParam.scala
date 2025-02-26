package ai.deepsense.deeplang.parameters.multivalue

import spray.json._

abstract class MultipleValuesParam[T] {

  def values: Seq[T]

}

object MultipleValuesParam extends DefaultJsonProtocol {

  val paramValuesKey = "values"

  val paramTypeKey = "type"

  val paramInstanceKey = "value"

  def isMultiValParam(value: JsValue): Boolean = value match {
    case JsObject(fields) => fields.contains(paramValuesKey)
    case _                => false
  }

  def fromJson[T](value: JsValue)(implicit format: JsonFormat[T]): MultipleValuesParam[T] = {
    val jsObject = value.asJsObject("MultipleValuesParam json object expected.")
    jsObject.fields.get(paramValuesKey) match {
      case Some(JsArray(elements)) =>
        CombinedMultipleValuesParam(elements.map(_fromJson[T](_)(format)))
      case Some(_)                 => deserializationError(s"$paramValuesKey should be Array.")
      case None                    => missignKeyException(paramValuesKey)
    }
  }

  private def _fromJson[T](jsValue: JsValue)(implicit format: JsonFormat[T]): MultipleValuesParam[T] = {

    val jsObject                  = jsValue.asJsObject("Instance of MultipleValuesParam json object expected.")
    val paramType: Option[String] = jsObject.fields.get(paramTypeKey).map(_.convertTo[String])
    paramType match {
      case Some(ValuesSequenceParam.paramType) =>
        val paramInstance = jsObject.fields.get(paramInstanceKey)
        paramInstance match {
          case Some(paramInstanceObject) =>
            paramInstanceObject.convertTo(ValuesSequenceParamJsonProtocol.valuesSequenceParamFormat[T](format))
          case None                      => missignKeyException(paramInstanceKey)
        }
      case Some(t)                             => deserializationError(s"Unsupported multiple values param type: $t")
      case None                                => missignKeyException(paramTypeKey)
    }
  }

  private def missignKeyException(key: String): Nothing =
    deserializationError(s"Missing key: $key")

}
