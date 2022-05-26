package io.deepsense.commons.json

import spray.json.DefaultJsonProtocol._
import spray.json.RootJsonFormat

import io.deepsense.commons.json.IdJsonProtocol._
import io.deepsense.commons.rest.client.req.NotebookClientRequest

trait NotebookRestClientProtocol {

  implicit val reqFormat: RootJsonFormat[NotebookClientRequest] =
    jsonFormat(NotebookClientRequest, "workflow_id", "node_id", "language")

}

object NotebookRestClientProtocol extends NotebookRestClientProtocol
