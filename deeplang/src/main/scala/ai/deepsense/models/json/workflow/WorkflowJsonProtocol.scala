package ai.deepsense.models.json.workflow

import spray.httpx.SprayJsonSupport
import spray.json.DefaultJsonProtocol

import ai.deepsense.commons.exception.json.FailureDescriptionJsonProtocol
import ai.deepsense.commons.json.DateTimeJsonProtocol
import ai.deepsense.commons.json.EnumerationSerializer
import ai.deepsense.commons.json.IdJsonProtocol
import ai.deepsense.models.json.graph.DKnowledgeJsonProtocol
import ai.deepsense.models.json.graph.NodeJsonProtocol
import ai.deepsense.models.json.graph.NodeStatusJsonProtocol
import ai.deepsense.models.workflows.Workflow
import ai.deepsense.models.workflows.WorkflowMetadata
import ai.deepsense.models.workflows.WorkflowType

trait WorkflowJsonProtocol
    extends DefaultJsonProtocol
    with SprayJsonSupport
    with NodeJsonProtocol
    with NodeStatusJsonProtocol
    with DKnowledgeJsonProtocol
    with ActionsJsonProtocol
    with IdJsonProtocol
    with FailureDescriptionJsonProtocol
    with DateTimeJsonProtocol
    with InferenceErrorJsonProtocol
    with InferenceWarningJsonProtocol
    with WorkflowInfoJsonProtocol
    with GraphJsonProtocol {

  implicit val workflowTypeFormat = EnumerationSerializer.jsonEnumFormat(WorkflowType)

  implicit val workflowMetadataFormat = jsonFormat(WorkflowMetadata, "type", "apiVersion")

  implicit val workflowFormat = jsonFormat(Workflow.apply, "metadata", "workflow", "thirdPartyData")

}
