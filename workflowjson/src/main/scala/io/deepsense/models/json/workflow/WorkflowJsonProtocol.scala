package io.deepsense.models.json.workflow

import spray.httpx.SprayJsonSupport
import spray.json._

import io.deepsense.commons.exception.json.FailureDescriptionJsonProtocol
import io.deepsense.commons.json.{DateTimeJsonProtocol, EnumerationSerializer, IdJsonProtocol}
import io.deepsense.models.json.graph.{DKnowledgeJsonProtocol, NodeJsonProtocol, NodeStatusJsonProtocol}
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
