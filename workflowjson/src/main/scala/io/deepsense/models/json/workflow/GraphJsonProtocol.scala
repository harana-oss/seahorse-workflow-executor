package io.deepsense.models.json.workflow

import spray.json._

import io.deepsense.graph.DeeplangGraph
import io.deepsense.models.json.graph.GraphJsonProtocol.GraphReader
import io.deepsense.models.json.graph.GraphJsonProtocol.GraphWriter

trait GraphJsonProtocol {

  protected def graphReader: GraphReader

  implicit def graphFormat: JsonFormat[DeeplangGraph] = new JsonFormat[DeeplangGraph] {

    override def read(json: JsValue): DeeplangGraph = json.convertTo[DeeplangGraph](graphReader)

    override def write(obj: DeeplangGraph): JsValue = obj.toJson(GraphWriter)

  }

}
