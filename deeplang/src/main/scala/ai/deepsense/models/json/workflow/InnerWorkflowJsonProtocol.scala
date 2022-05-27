package ai.deepsense.models.json.workflow

import spray.httpx.SprayJsonSupport
import spray.json.DefaultJsonProtocol
import spray.json.JsValue

import ai.deepsense.commons.json.DateTimeJsonProtocol
import ai.deepsense.commons.json.IdJsonProtocol
import ai.deepsense.deeplang.parameters.custom.InnerWorkflow
import ai.deepsense.deeplang.parameters.custom.PublicParam
import ai.deepsense.models.json.graph.GraphJsonProtocol.GraphReader
import ai.deepsense.models.json.graph.DKnowledgeJsonProtocol
import ai.deepsense.models.json.graph.NodeJsonProtocol
import ai.deepsense.models.json.graph.NodeStatusJsonProtocol

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

trait WriteInnerWorkflowJsonProtocol
    extends DefaultJsonProtocol
    with SprayJsonSupport
    with NodeJsonProtocol
    with NodeStatusJsonProtocol
    with DKnowledgeJsonProtocol
    with IdJsonProtocol
    with DateTimeJsonProtocol
    with WriteGraphJsonProtocol {

  implicit val publicParamFormat = jsonFormat(PublicParam.apply, "nodeId", "paramName", "publicName")

  implicit val innerWorkflowFormat = jsonFormat(InnerWorkflow.apply, "workflow", "thirdPartyData", "publicParams")

}

class InnerWorkflowJsonReader(override val graphReader: GraphReader) extends InnerWorkflowJsonProtocol {

  def toInner(jsValue: JsValue) =
    jsValue.convertTo[InnerWorkflow]

}

object InnerWorkflowJsonReader {

  def toInner(jsValue: JsValue, graphReader: GraphReader) = {
    val innerWorkflowJsonReader = new InnerWorkflowJsonReader(graphReader)
    innerWorkflowJsonReader.toInner(jsValue)
  }

}
