package io.deepsense.models.json.graph

import spray.json._

import io.deepsense.graph.{Edge, Endpoint}

trait EdgeJsonProtocol extends DefaultJsonProtocol {

  import NodeJsonProtocol._

  implicit val EndpointFormat = jsonFormat2(Endpoint)
  implicit val EdgeFormat = jsonFormat2(Edge.apply)
}

object EdgeJsonProtocol extends EdgeJsonProtocol
