package io.deepsense.workflowexecutor.communication.mq.json

import spray.json.JsObject

trait JsonMessageDeserializer {
  def deserialize: PartialFunction[(String, JsObject), Any]
}
