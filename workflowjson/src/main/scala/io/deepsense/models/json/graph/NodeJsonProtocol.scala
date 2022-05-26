package io.deepsense.models.json.graph

import io.deepsense.commons.json.IdJsonProtocol
import io.deepsense.graph.DeeplangGraph.DeeplangNode
import spray.json._

trait NodeJsonProtocol extends DefaultJsonProtocol with IdJsonProtocol {

  import OperationJsonProtocol.DOperationWriter

  implicit object NodeWriter extends JsonWriter[DeeplangNode] {

    override def write(node: DeeplangNode): JsValue = JsObject(
      Map(NodeJsonProtocol.Id -> node.id.toJson) ++
        node.value.toJson.asJsObject.fields
    )

  }

}

object NodeJsonProtocol extends NodeJsonProtocol {

  val Id = "id"

}
