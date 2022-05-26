package ai.deepsense.models.json.workflow

import spray.json.JsValue
import spray.json.JsonFormat

import ai.deepsense.graph.DeeplangGraph
import ai.deepsense.models.json.graph.GraphJsonProtocol.GraphWriter

trait WriteGraphJsonProtocol {

  implicit def graphFormat: JsonFormat[DeeplangGraph] = new JsonFormat[DeeplangGraph] {

    override def read(json: JsValue): DeeplangGraph = ???

    override def write(obj: DeeplangGraph): JsValue = obj.toJson(GraphWriter)

  }

}
