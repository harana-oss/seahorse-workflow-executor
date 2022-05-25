package io.deepsense.commons.exception.json

import spray.httpx.SprayJsonSupport
import spray.json._

import io.deepsense.commons.exception.{FailureCode, FailureDescription}
import io.deepsense.commons.json.{EnumerationSerializer, IdJsonProtocol}

trait FailureDescriptionJsonProtocol
    extends DefaultJsonProtocol
    with IdJsonProtocol
    with SprayJsonSupport {

  implicit val failureCodeFormat = EnumerationSerializer.jsonEnumFormat(FailureCode)
  implicit val failureDescriptionFormat = jsonFormat5(FailureDescription.apply)
}

object FailureDescriptionJsonProtocol extends FailureDescriptionJsonProtocol
