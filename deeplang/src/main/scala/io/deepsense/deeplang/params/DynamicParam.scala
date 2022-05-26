package io.deepsense.deeplang.params

import spray.json.DefaultJsonProtocol._
import spray.json._

import io.deepsense.deeplang.params.ParameterType.ParameterType

/** This parameter type is used to forward parameters from the DOperation to its input DOperables. Value of this
  * parameter is a Json that can be inserted as values of parameters of DOperable that is used in DOperation. For
  * example, DOperation Transform dynamically renders parameters of its input Transformer.
  * @param inputPort
  *   number of port on which the DOperable is received
  */
class DynamicParam(override val name: String, override val description: Option[String], val inputPort: Int)
    extends Param[JsValue] {

  override protected def extraJsFields: Map[String, JsValue] = Map("inputPort" -> inputPort.toJson)

  override val parameterType: ParameterType = ParameterType.Dynamic

  override def valueToJson(value: JsValue): JsValue = value

  override def valueFromJson(jsValue: JsValue): JsValue = {
    // It makes no sense to store JsNull values in DynamicParameter's value.
    // No value has the same meaning as a JsNull.
    // Storing JsNulls makes comparing DynamicParameters' values hard.
    // For example: {} and {'someParam': null} have the same meaning but they are not equal.
    // Thus, we are removing null values from the json to achieve sane equality test results.
    def removeNullValues(jsValue: JsValue): JsValue =
      jsValue match {
        case JsObject(fields) =>
          val cleanedUpFields = fields.collect {
            case (key, value) if value != JsNull =>
              (key, removeNullValues(value))
          }
          JsObject(cleanedUpFields)
        case x => x
      }
    removeNullValues(jsValue)
  }

  override def replicate(name: String): DynamicParam =
    new DynamicParam(name, description, inputPort)

}
