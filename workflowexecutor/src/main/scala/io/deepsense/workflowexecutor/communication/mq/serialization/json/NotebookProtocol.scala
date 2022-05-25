package io.deepsense.workflowexecutor.communication.mq.serialization.json

import io.deepsense.workflowexecutor.communication.message.notebook.KernelManagerReady
import io.deepsense.workflowexecutor.communication.message.notebook.KernelManagerReadyJsonProtocol._
import io.deepsense.workflowexecutor.communication.mq.json.DefaultJsonMessageDeserializer

object NotebookProtocol {
  val kernelManagerReady = "kernelManagerReady"

  object KernelManagerReadyDeserializer
    extends DefaultJsonMessageDeserializer[KernelManagerReady](kernelManagerReady)
}
