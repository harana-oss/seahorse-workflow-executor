package io.deepsense.commons.exception

import spray.json._

import io.deepsense.commons.StandardSpec
import io.deepsense.commons.exception.json.FailureDescriptionJsonProtocol
import io.deepsense.commons.serialization.Serialization

class FailureDescriptionSerializationSpec extends StandardSpec with FailureDescriptionJsonProtocol with Serialization {

  val id = DeepSenseFailure.Id.randomId

  val failureDescription = FailureDescription(
    id,
    FailureCode.UnexpectedError,
    "Very descriptive title",
    Some("Very descriptive message"),
    Map(
      "count"    -> "100",
      "nodes[0]" -> "u1",
      "nodes[1]" -> "u2",
      "nodes[2]" -> "u3",
      "desc.a"   -> "1",
      "desc.b"   -> "foo"
    )
  )

  val failureDescriptionJson: JsObject = JsObject(
    "id"      -> JsString(id.toString),
    "code"    -> JsString(failureDescription.code.toString),
    "title"   -> JsString(failureDescription.title),
    "message" -> JsString(failureDescription.message.get),
    "details" -> JsObject(
      "count"    -> JsString("100"),
      "nodes[0]" -> JsString("u1"),
      "nodes[1]" -> JsString("u2"),
      "nodes[2]" -> JsString("u3"),
      "desc.a"   -> JsString("1"),
      "desc.b"   -> JsString("foo")
    )
  )

  "FailureDescription" should {
    "serialize to Json" in {
      val json = failureDescription.toJson
      json shouldBe failureDescriptionJson
    }

    "deserialize from Json" in {
      val deserialized = failureDescriptionJson.convertTo[FailureDescription]
      deserialized shouldBe failureDescription
    }

    "serialize and deserialize using Java serialization" in {
      val serialized   = serialize(failureDescription)
      val deserialized = deserialize[FailureDescription](serialized)
      deserialized shouldBe failureDescription
    }
  }

}
