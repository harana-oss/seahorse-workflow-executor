package ai.deepsense.deeplang.parameters

import spray.json._

import ai.deepsense.models.json.graph.GraphJsonProtocol.GraphReader

abstract class ParameterWithJsFormat[T: JsonFormat] extends Parameter[T] {

  override def valueToJson(value: T): JsValue = value.toJson

  override def valueFromJson(jsValue: JsValue, graphReader: GraphReader): T = jsValue.convertTo[T]

}
