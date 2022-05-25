package io.deepsense.workflowexecutor.communication.mq.json

import spray.json.{JsObject, JsonReader}

class DefaultJsonMessageDeserializer[T : JsonReader](handledName: String)
    extends JsonMessageDeserializer {

  val deserialize: PartialFunction[(String, JsObject), Any] = {
    case (name, body) if isHandled(name) => handle(body)
  }

  private def isHandled(name: String): Boolean = name == handledName
  private def handle(body: JsObject): Any = body.convertTo[T]
}
