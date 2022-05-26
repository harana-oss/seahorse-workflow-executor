package io.deepsense.commons.json.envelope

import spray.json._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class EnvelopeSpec extends AnyFlatSpec with Matchers with DefaultJsonProtocol {

  val StringLabel = "ExampleOfStringLabel"

  val WrongStringLabel = "WrongExampleOfStringLabel"

  val EnvelopeStringJsonFormat = new EnvelopeJsonFormat[String](StringLabel)

  "Envelope[String]" should "be encoded to and decoded from JSON" in {
    val exampleString   = "Johny Bravo"
    val envelope        = Envelope(exampleString)
    val encodedEnvelope = EnvelopeStringJsonFormat.write(envelope)
    encodedEnvelope shouldBe JsObject(StringLabel -> JsString(exampleString))
    val decodedEnvelope = EnvelopeStringJsonFormat.read(encodedEnvelope.toJson)
    decodedEnvelope.content shouldBe exampleString
  }

  "Wrongly structured Envelope[String] JSON" should "throw exception during decoding" in {
    val emptyJsonSet = JsObject()
    an[DeserializationException] should
      be thrownBy EnvelopeStringJsonFormat.read(emptyJsonSet.toJson)
  }

  "Wrongly labeled Envelope[String] JSON" should "throw exception during decoding" in {
    val wrongLabeledJson = JsObject(WrongStringLabel -> JsString(""))
    an[DeserializationException] should be thrownBy EnvelopeStringJsonFormat.read(wrongLabeledJson.toJson)
  }

}
