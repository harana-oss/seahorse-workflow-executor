package ai.deepsense.workflowexecutor.rabbitmq

import akka.actor.Actor
import akka.actor.Props

import ai.deepsense.commons.utils.Logging

class PublisherActor(topic: String, publisher: MQPublisher) extends Actor with Logging {

  override def receive: Receive = { case message: Any =>
    logger.info(
      "PublisherActor for topic: {} receives message {} from '{}'",
      topic,
      message.getClass.getName,
      sender().path.name
    )
    publisher.publish(topic, message)
  }

}

object PublisherActor {

  def props(topic: String, publisher: MQPublisher): Props =
    Props(new PublisherActor(topic, publisher))

}
