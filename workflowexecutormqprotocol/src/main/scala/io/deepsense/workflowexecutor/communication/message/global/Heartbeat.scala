package io.deepsense.workflowexecutor.communication.message.global

import spray.httpx.SprayJsonSupport
import spray.json.DefaultJsonProtocol

case class Heartbeat(workflowId: String)

trait HeartbeatJsonProtocol extends DefaultJsonProtocol with SprayJsonSupport {

  implicit val heartbeatFormat = jsonFormat1(Heartbeat)

}

object HeartbeatJsonProtocol extends HeartbeatJsonProtocol
