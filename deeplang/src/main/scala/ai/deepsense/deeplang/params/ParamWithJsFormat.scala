package ai.deepsense.deeplang.params

import spray.json._

import ai.deepsense.models.json.graph.GraphJsonProtocol.GraphReader

abstract class ParamWithJsFormat[T: JsonFormat] extends Param[T] {

  override def valueToJson(value: T): JsValue = value.toJson

  override def valueFromJson(jsValue: JsValue, graphReader: GraphReader): T = jsValue.convertTo[T]

}
