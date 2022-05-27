package ai.deepsense.models.json.workflow

import spray.json.JsValue
import spray.json.JsonFormat

import ai.deepsense.graph.FlowGraph
import ai.deepsense.models.json.graph.GraphJsonProtocol.GraphReader
import ai.deepsense.models.json.graph.GraphJsonProtocol.GraphWriter

trait GraphJsonProtocol {

  protected def graphReader: GraphReader

  implicit def graphFormat: JsonFormat[FlowGraph] = new JsonFormat[FlowGraph] {

    override def read(json: JsValue): FlowGraph = json.convertTo[FlowGraph](graphReader)

    override def write(obj: FlowGraph): JsValue = obj.toJson(GraphWriter)

  }

}
