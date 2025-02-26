package ai.deepsense.deeplang.parameters

import spray.json.DefaultJsonProtocol._
import spray.json._

import ai.deepsense.deeplang.parameters.ParameterType.ParameterType
import ai.deepsense.models.json.graph.GraphJsonProtocol.GraphReader

/** This parameter type is used to forward parameters from the Action to its input ActionObjects. Value of this
  * parameter is a Json that can be inserted as values of parameters of ActionObject that is used in Action. For
  * example, Action Transform dynamically renders parameters of its input Transformer.
  * @param inputPort
  *   number of port on which the ActionObject is received
  */
class DynamicParameter(override val name: String, override val description: Option[String], val inputPort: Int)
    extends Parameter[JsValue] {

  override protected def extraJsFields: Map[String, JsValue] = Map("inputPort" -> inputPort.toJson)

  override val parameterType: ParameterType = ParameterType.Dynamic

  override def valueToJson(value: JsValue): JsValue = value

  override def valueFromJson(jsValue: JsValue, graphReader: GraphReader): JsValue = {
    // It makes no sense to store JsNull values in DynamicParameter's value.
    // No value has the same meaning as a JsNull.
    // Storing JsNulls makes comparing DynamicParameters' values hard.
    // For example: {} and {'someParam': null} have the same meaning but they are not equal.
    // Thus, we are removing null values from the json to achieve sane equality test results.
    def removeNullValues(jsValue: JsValue): JsValue = {
      jsValue match {
        case JsObject(fields) =>
          val cleanedUpFields = fields.collect {
            case (key, value) if value != JsNull =>
              (key, removeNullValues(value))
          }
          JsObject(cleanedUpFields)
        case x                => x
      }
    }
    removeNullValues(jsValue)
  }

  override def replicate(name: String): DynamicParameter =
    new DynamicParameter(name, description, inputPort)

}
