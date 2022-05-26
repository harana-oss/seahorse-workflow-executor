package ai.deepsense.workflowexecutor.communication.mq.json

import java.nio.charset.Charset

import spray.json.JsObject

import ai.deepsense.workflowexecutor.communication.mq.MQSerializer

class JsonMQSerializer(
    jsonSerializers: Seq[JsonMessageSerializer],
    parent: Option[JsonMQSerializer] = None
) extends MQSerializer
    with JsonMessageSerializer {

  private val combinedJsonSerializers = {
    jsonSerializers.tail.foldLeft(jsonSerializers.head.serialize) { case (acc, serializer) =>
      acc.orElse(serializer.serialize)
    }
  }

  override val serialize: PartialFunction[Any, JsObject] = {
    parent match {
      case Some(p) => combinedJsonSerializers.orElse(p.serialize)
      case None    => combinedJsonSerializers
    }
  }

  override def serializeMessage(message: Any): Array[Byte] =
    serialize(message).compactPrint.getBytes(Global.charset)

  def orElse(next: JsonMQSerializer): JsonMQSerializer =
    new JsonMQSerializer(jsonSerializers, Some(next))

}
