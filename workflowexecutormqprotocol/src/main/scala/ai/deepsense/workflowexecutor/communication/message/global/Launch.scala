package ai.deepsense.workflowexecutor.communication.message.global

import spray.httpx.SprayJsonSupport
import spray.json.DefaultJsonProtocol

import ai.deepsense.commons.json.IdJsonProtocol
import ai.deepsense.graph.Node
import ai.deepsense.models.workflows.Workflow

case class Launch(workflowId: Workflow.Id, nodesToExecute: Set[Node.Id])

trait LaunchJsonProtocol extends DefaultJsonProtocol with IdJsonProtocol with SprayJsonSupport {

  implicit val launchFormat = jsonFormat2(Launch)

}

object LaunchJsonProtocol extends LaunchJsonProtocol
