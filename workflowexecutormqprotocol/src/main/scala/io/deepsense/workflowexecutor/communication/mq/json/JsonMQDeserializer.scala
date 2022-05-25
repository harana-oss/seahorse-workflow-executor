package io.deepsense.workflowexecutor.communication.mq.json

import java.nio.charset.Charset

import spray.json._

import io.deepsense.workflowexecutor.communication.mq.MQDeserializer
import io.deepsense.workflowexecutor.communication.mq.json.Constants.JsonKeys._

class JsonMQDeserializer(
    jsonDeserializers: Seq[JsonMessageDeserializer],
    parent: Option[JsonMQDeserializer] = None)
  extends MQDeserializer with JsonMessageDeserializer {

  private val combinedJsonDeserializers = {
    jsonDeserializers.tail.foldLeft(jsonDeserializers.head.deserialize) {
      case (acc, deserializer) =>
        acc.orElse(deserializer.deserialize)
    }
  }

  override val deserialize: PartialFunction[(String, JsObject), Any] = {
    parent match {
      case Some(p) => combinedJsonDeserializers.orElse(p.deserialize)
      case None => combinedJsonDeserializers
    }
  }

  override def deserializeMessage(data: Array[Byte]): Any = {
    val json = new String(data, Global.charset).parseJson
    val jsObject = json.asJsObject
    val fields = jsObject.fields
    import spray.json.DefaultJsonProtocol._
    val messageType = getField(fields, messageTypeKey).convertTo[String]
    val body = getField(fields, messageBodyKey).asJsObject()
    deserialize(messageType, body)
  }

  def orElse(next: JsonMQDeserializer): JsonMQDeserializer =
    new JsonMQDeserializer(jsonDeserializers, Some(next))

  private def getField(fields: Map[String, JsValue], fieldName: String): JsValue = {
    try {
      fields(fieldName)
    } catch {
      case e: NoSuchElementException =>
        throw new DeserializationException(s"Missing field: $fieldName", e)
    }
  }
}
