package io.deepsense.commons.json

import java.util.UUID

import spray.json._

import io.deepsense.commons.models.Id

trait IdJsonProtocol extends UUIDJsonProtocol {

  implicit object IdFormat extends RootJsonFormat[Id] {

    override def write(obj: Id): JsValue = obj.value.toJson

    override def read(json: JsValue): Id = json.convertTo[UUID]

  }

}

object IdJsonProtocol extends IdJsonProtocol
