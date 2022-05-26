package ai.deepsense.workflowexecutor.communication.mq.serialization.json

import ai.deepsense.workflowexecutor.communication.message.notebook.KernelManagerReady
import ai.deepsense.workflowexecutor.communication.message.notebook.KernelManagerReadyJsonProtocol._
import ai.deepsense.workflowexecutor.communication.mq.json.DefaultJsonMessageDeserializer

object NotebookProtocol {

  val kernelManagerReady = "kernelManagerReady"

  object KernelManagerReadyDeserializer extends DefaultJsonMessageDeserializer[KernelManagerReady](kernelManagerReady)

}
