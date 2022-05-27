package ai.deepsense.models.json.graph

import ai.deepsense.commons.json.IdJsonProtocol
import ai.deepsense.graph.FlowGraph.FlowNode
import spray.json._

trait NodeJsonProtocol extends DefaultJsonProtocol with IdJsonProtocol {

  import OperationJsonProtocol.ActionWriter

  implicit object NodeWriter extends JsonWriter[FlowNode] {

    override def write(node: FlowNode): JsValue = JsObject(
      Map(NodeJsonProtocol.Id -> node.id.toJson) ++
        node.value.toJson.asJsObject.fields
    )

  }

}

object NodeJsonProtocol extends NodeJsonProtocol {

  val Id = "id"

}
