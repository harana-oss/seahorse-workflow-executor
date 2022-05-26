package ai.deepsense.commons.json

import spray.json.DefaultJsonProtocol._
import spray.json.RootJsonFormat

import ai.deepsense.commons.json.IdJsonProtocol._
import ai.deepsense.commons.rest.client.req.NotebookClientRequest

trait NotebookRestClientProtocol {

  implicit val reqFormat: RootJsonFormat[NotebookClientRequest] =
    jsonFormat(NotebookClientRequest, "workflow_id", "node_id", "language")

}

object NotebookRestClientProtocol extends NotebookRestClientProtocol
