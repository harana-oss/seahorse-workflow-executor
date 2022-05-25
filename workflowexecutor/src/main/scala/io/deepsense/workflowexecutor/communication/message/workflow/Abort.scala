package io.deepsense.workflowexecutor.communication.message.workflow

import spray.json.RootJsonFormat

import io.deepsense.commons.json.IdJsonProtocol._
import io.deepsense.commons.utils.Logging
import io.deepsense.models.workflows.Workflow
import spray.json.DefaultJsonProtocol._

case class Abort(workflowId: Workflow.Id)

trait AbortJsonProtocol extends Logging {

  implicit val abortFormat: RootJsonFormat[Abort] = jsonFormat1(Abort.apply)
}

object AbortJsonProtocol extends AbortJsonProtocol
