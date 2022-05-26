package ai.deepsense.models.json.workflow

import spray.json.DefaultJsonProtocol

import ai.deepsense.commons.json.DateTimeJsonProtocol
import ai.deepsense.commons.json.IdJsonProtocol
import ai.deepsense.models.workflows.WorkflowInfo

trait WorkflowInfoJsonProtocol extends DefaultJsonProtocol with IdJsonProtocol with DateTimeJsonProtocol {

  implicit val workflowInfoFormat = jsonFormat7(WorkflowInfo.apply)

}
