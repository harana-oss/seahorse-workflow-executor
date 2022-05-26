package ai.deepsense.workflowexecutor.communication.message.workflow

import spray.httpx.SprayJsonSupport
import spray.json.DefaultJsonProtocol

case class Synchronize()

trait SynchronizeJsonProtocol extends DefaultJsonProtocol with SprayJsonSupport {

  implicit val synchronizeFormat = jsonFormat0(Synchronize)

}

object SynchronizeJsonProtocol extends SynchronizeJsonProtocol
