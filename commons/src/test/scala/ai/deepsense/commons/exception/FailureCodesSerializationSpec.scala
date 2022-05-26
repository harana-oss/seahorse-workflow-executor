package ai.deepsense.commons.exception

import ai.deepsense.commons.StandardSpec
import ai.deepsense.commons.exception.FailureCode.FailureCode
import ai.deepsense.commons.serialization.Serialization

class FailureCodesSerializationSpec extends StandardSpec with Serialization {

  "FailureCode" should {
    "serialize and deserialize" in {
      val code         = FailureCode.UnexpectedError
      val serialized   = serialize(code)
      val deserialized = deserialize[FailureCode](serialized)
      deserialized shouldBe code
    }
  }

}
