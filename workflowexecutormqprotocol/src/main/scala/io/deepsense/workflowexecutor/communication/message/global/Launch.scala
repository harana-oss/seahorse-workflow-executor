package io.deepsense.workflowexecutor.communication.message.global

import spray.httpx.SprayJsonSupport
import spray.json.DefaultJsonProtocol

import io.deepsense.commons.json.IdJsonProtocol
import io.deepsense.graph.Node
import io.deepsense.models.workflows.Workflow

case class Launch(workflowId: Workflow.Id, nodesToExecute: Set[Node.Id])

trait LaunchJsonProtocol extends DefaultJsonProtocol
  with IdJsonProtocol
  with SprayJsonSupport {
  implicit val launchFormat = jsonFormat2(Launch)
}

object LaunchJsonProtocol extends LaunchJsonProtocol
