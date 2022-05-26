package ai.deepsense.deeplang.params.choice

import spray.json._

import ai.deepsense.deeplang.params.Params

abstract class Choice extends Params {

  val name: String

  val choiceOrder: List[Class[_ <: Choice]]

  def toJson: JsValue = JsObject("name" -> name.toJson, "schema" -> paramsToJson)

}
