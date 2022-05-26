package io.deepsense.workflowexecutor.communication.message.global

import spray.httpx.SprayJsonSupport
import spray.json.DefaultJsonProtocol

case class PoisonPill()

trait PoisonPillJsonProtocol extends DefaultJsonProtocol with SprayJsonSupport {

  implicit val poisonPillFormat = jsonFormat0(PoisonPill)

}

object PoisonPillJsonProtocol extends PoisonPillJsonProtocol
