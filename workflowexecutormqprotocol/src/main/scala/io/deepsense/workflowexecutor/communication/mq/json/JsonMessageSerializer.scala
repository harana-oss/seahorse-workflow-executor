package io.deepsense.workflowexecutor.communication.mq.json

import spray.json.JsObject

trait JsonMessageSerializer {

  def serialize: PartialFunction[Any, JsObject]

}
