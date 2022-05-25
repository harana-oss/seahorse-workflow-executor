package io.deepsense.workflowexecutor.communication.mq.serialization.json

import io.deepsense.models.json.graph.GraphJsonProtocol.GraphReader
import io.deepsense.workflowexecutor.communication.mq.json.JsonMQSerializer

case class ProtocolJsonSerializer(graphReader: GraphReader)
  extends JsonMQSerializer(
    Seq(WorkflowProtocol.SynchronizeSerializer))
