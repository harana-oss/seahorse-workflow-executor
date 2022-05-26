package io.deepsense.workflowexecutor.communication.message.global

import spray.httpx.SprayJsonSupport
import spray.json.DefaultJsonProtocol

case class Ready(sessionId: String)

trait ReadyJsonProtocol extends DefaultJsonProtocol with SprayJsonSupport {

  implicit val readyFormat = jsonFormat1(Ready)

}

object ReadyJsonProtocol extends ReadyJsonProtocol
