package io.deepsense.workflowexecutor.communication.mq

trait MQSerializer {

  def serializeMessage(message: Any): Array[Byte]
}
