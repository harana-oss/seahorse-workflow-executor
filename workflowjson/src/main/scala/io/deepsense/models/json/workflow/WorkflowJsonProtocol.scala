package io.deepsense.models.json.workflow

import spray.httpx.SprayJsonSupport
import spray.json._

import io.deepsense.commons.exception.json.FailureDescriptionJsonProtocol
import io.deepsense.commons.json.DateTimeJsonProtocol
import io.deepsense.commons.json.EnumerationSerializer
import io.deepsense.commons.json.IdJsonProtocol
import io.deepsense.models.json.graph.DKnowledgeJsonProtocol
import io.deepsense.models.json.graph.NodeJsonProtocol
import io.deepsense.models.json.graph.NodeStatusJsonProtocol
import io.deepsense.models.workflows._

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
