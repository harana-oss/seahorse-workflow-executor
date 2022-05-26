package ai.deepsense.commons.json

import java.util.UUID

import spray.httpx.SprayJsonSupport
import spray.json._

trait UUIDJsonProtocol {

  implicit object UUIDFormat extends JsonFormat[UUID] with SprayJsonSupport {

    override def write(obj: UUID): JsValue = JsString(obj.toString)

    override def read(json: JsValue): UUID = json match {
      case JsString(x) => UUID.fromString(x)
      case x           => deserializationError(s"Expected UUID as JsString, but got $x")
    }

  }

}

object UUIDJsonProtocol extends UUIDJsonProtocol
