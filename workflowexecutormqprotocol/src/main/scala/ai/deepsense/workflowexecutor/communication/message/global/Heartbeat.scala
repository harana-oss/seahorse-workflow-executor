package ai.deepsense.workflowexecutor.communication.message.global

import spray.httpx.SprayJsonSupport
import spray.json.DefaultJsonProtocol

// TODO move sparkUiAddress to separate message
case class Heartbeat(workflowId: String, sparkUiAddress: Option[String])

trait HeartbeatJsonProtocol extends DefaultJsonProtocol with SprayJsonSupport {

  implicit val heartbeatFormat = jsonFormat2(Heartbeat)

}

object HeartbeatJsonProtocol extends HeartbeatJsonProtocol
