package io.deepsense.commons

import org.slf4j.Logger

import io.deepsense.commons.serialization.Serialization
import io.deepsense.commons.utils.Logging

class LoggingSerializationSpec extends StandardSpec with UnitTestSupport with Serialization {

  "Object" when {
    "mixes-in SerializableLogging" should {
      "be serializable" in {
        val testObject = new SerializableTestObject()
        testObject.getLogger.trace("Logging just to force initiation of lazy logger")
        val deserialized = serializeDeserialize[SerializableTestObject](testObject)
        deserialized.getLogger should not be null
        deserialized.getLogger.trace("If this is printed everything is OK")
      }
    }
  }

}

class SerializableTestObject extends Serializable with Logging {

  def getLogger: Logger = this.logger

}
