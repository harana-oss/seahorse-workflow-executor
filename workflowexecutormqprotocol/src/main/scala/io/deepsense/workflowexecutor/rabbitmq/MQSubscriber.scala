package io.deepsense.workflowexecutor.rabbitmq

import akka.actor.ActorRef
import com.newmotion.akka.rabbitmq.BasicProperties
import com.newmotion.akka.rabbitmq.Channel
import com.newmotion.akka.rabbitmq.DefaultConsumer
import com.newmotion.akka.rabbitmq.Envelope

import io.deepsense.commons.serialization.Serialization
import io.deepsense.commons.utils.Logging
import io.deepsense.workflowexecutor.communication.mq.MQDeserializer

case class MQSubscriber(
    subscriberActor: ActorRef,
    mqMessageDeserializer: MQDeserializer,
    channel: Channel
) extends DefaultConsumer(channel)
    with Logging
    with Serialization {

  override def handleDelivery(
      consumerTag: String,
      envelope: Envelope,
      properties: BasicProperties,
      body: Array[Byte]
  ): Unit =
    try
      subscriberActor ! mqMessageDeserializer.deserializeMessage(body)
    catch {
      case e: Exception => logger.error("Message deserialization failed", e)
    }

}
