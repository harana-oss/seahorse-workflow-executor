package ai.deepsense.models.json.workflow

import spray.json.JsValue
import spray.json.JsonFormat

import ai.deepsense.graph.FlowGraph
import ai.deepsense.models.json.graph.GraphJsonProtocol.GraphWriter

trait WriteGraphJsonProtocol {

  implicit def graphFormat: JsonFormat[FlowGraph] = new JsonFormat[FlowGraph] {

    override def read(json: JsValue): FlowGraph = ???

    override def write(obj: FlowGraph): JsValue = obj.toJson(GraphWriter)

  }

}
