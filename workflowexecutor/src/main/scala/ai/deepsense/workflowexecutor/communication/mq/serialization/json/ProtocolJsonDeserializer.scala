package ai.deepsense.workflowexecutor.communication.mq.serialization.json

import ai.deepsense.models.json.graph.GraphJsonProtocol.GraphReader
import ai.deepsense.workflowexecutor.communication.mq.json.JsonMQDeserializer

case class ProtocolJsonDeserializer(graphReader: GraphReader)
    extends JsonMQDeserializer(
      Seq(
        WorkflowProtocol.AbortDeserializer,
        WorkflowProtocol.UpdateWorkflowDeserializer(graphReader),
        WorkflowProtocol.SynchronizeDeserializer,
        NotebookProtocol.KernelManagerReadyDeserializer
      )
    )
