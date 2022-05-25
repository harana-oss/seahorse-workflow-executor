package io.deepsense.deeplang.params.choice

import spray.json.DefaultJsonProtocol.StringJsonFormat
import spray.json._

import io.deepsense.deeplang.params.{Param, Params}

abstract class Choice extends Params {
  val name: String

  val choiceOrder: List[Class[_ <: Choice]]

  def toJson: JsValue = JsObject("name" -> name.toJson, "schema" -> paramsToJson)
}
