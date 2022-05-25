package io.deepsense.models.json.workflow

import spray.json.DefaultJsonProtocol

import io.deepsense.commons.json.{DateTimeJsonProtocol, IdJsonProtocol}
import io.deepsense.models.workflows.WorkflowInfo

trait WorkflowInfoJsonProtocol
  extends DefaultJsonProtocol
  with IdJsonProtocol
  with DateTimeJsonProtocol {

  implicit val workflowInfoFormat = jsonFormat7(WorkflowInfo.apply)
}
