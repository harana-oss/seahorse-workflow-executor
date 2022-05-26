package io.deepsense.models.json.workflow

import spray.httpx.SprayJsonSupport
import spray.json._

import io.deepsense.commons.json.DateTimeJsonProtocol
import io.deepsense.commons.json.IdJsonProtocol
import io.deepsense.deeplang.params.custom.PublicParam
import io.deepsense.deeplang.params.custom.InnerWorkflow
import io.deepsense.models.json.graph.DKnowledgeJsonProtocol
import io.deepsense.models.json.graph.NodeJsonProtocol
import io.deepsense.models.json.graph.NodeStatusJsonProtocol

trait InnerWorkflowJsonProtocol
    extends DefaultJsonProtocol
    with SprayJsonSupport
    with NodeJsonProtocol
    with NodeStatusJsonProtocol
    with DKnowledgeJsonProtocol
    with IdJsonProtocol
    with DateTimeJsonProtocol
    with GraphJsonProtocol {

  implicit val publicParamFormat = jsonFormat(PublicParam.apply, "nodeId", "paramName", "publicName")

  implicit val innerWorkflowFormat = jsonFormat(InnerWorkflow.apply, "workflow", "thirdPartyData", "publicParams")

}
