package ai.deepsense.workflowexecutor.communication.mq

trait MQDeserializer {

  def deserializeMessage(data: Array[Byte]): Any

}
