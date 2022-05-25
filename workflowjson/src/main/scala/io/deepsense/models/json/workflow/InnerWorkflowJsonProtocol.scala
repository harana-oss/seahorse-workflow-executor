package io.deepsense.models.json.workflow

import spray.httpx.SprayJsonSupport
import spray.json._

import io.deepsense.commons.json.{DateTimeJsonProtocol, IdJsonProtocol}
import io.deepsense.deeplang.params.custom.{PublicParam, InnerWorkflow}
import io.deepsense.models.json.graph.{DKnowledgeJsonProtocol, NodeJsonProtocol, NodeStatusJsonProtocol}

trait InnerWorkflowJsonProtocol
  extends DefaultJsonProtocol
  with SprayJsonSupport
  with NodeJsonProtocol
  with NodeStatusJsonProtocol
  with DKnowledgeJsonProtocol
  with IdJsonProtocol
  with DateTimeJsonProtocol
  with GraphJsonProtocol {

  implicit val publicParamFormat = jsonFormat(
    PublicParam.apply, "nodeId", "paramName", "publicName")

  implicit val innerWorkflowFormat = jsonFormat(
    InnerWorkflow.apply, "workflow", "thirdPartyData", "publicParams")

}
