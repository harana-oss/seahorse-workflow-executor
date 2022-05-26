package ai.deepsense.models.json.workflow

import spray.json.JsValue
import spray.json.JsonFormat

import ai.deepsense.graph.DeeplangGraph
import ai.deepsense.models.json.graph.GraphJsonProtocol.GraphReader
import ai.deepsense.models.json.graph.GraphJsonProtocol.GraphWriter

trait GraphJsonProtocol {

  protected def graphReader: GraphReader

  implicit def graphFormat: JsonFormat[DeeplangGraph] = new JsonFormat[DeeplangGraph] {

    override def read(json: JsValue): DeeplangGraph = json.convertTo[DeeplangGraph](graphReader)

    override def write(obj: DeeplangGraph): JsValue = obj.toJson(GraphWriter)

  }

}
