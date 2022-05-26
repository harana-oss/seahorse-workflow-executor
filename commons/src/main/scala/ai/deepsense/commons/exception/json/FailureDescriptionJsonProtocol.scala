package ai.deepsense.commons.exception.json

import spray.httpx.SprayJsonSupport
import spray.json._

import ai.deepsense.commons.exception.FailureCode
import ai.deepsense.commons.exception.FailureDescription
import ai.deepsense.commons.json.EnumerationSerializer
import ai.deepsense.commons.json.IdJsonProtocol

trait FailureDescriptionJsonProtocol extends DefaultJsonProtocol with IdJsonProtocol with SprayJsonSupport {

  implicit val failureCodeFormat = EnumerationSerializer.jsonEnumFormat(FailureCode)

  implicit val failureDescriptionFormat = jsonFormat5(FailureDescription.apply)

}

object FailureDescriptionJsonProtocol extends FailureDescriptionJsonProtocol
