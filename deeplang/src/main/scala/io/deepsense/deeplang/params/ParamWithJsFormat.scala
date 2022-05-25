package io.deepsense.deeplang.params

import spray.json._

abstract class ParamWithJsFormat[T: JsonFormat] extends Param[T] {
  def valueToJson(value: T): JsValue = value.toJson
  def valueFromJson(jsValue: JsValue): T = jsValue.convertTo[T]
}
