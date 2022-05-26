package ai.deepsense.workflowexecutor.communication.mq.serialization.json

import ai.deepsense.models.json.graph.GraphJsonProtocol.GraphReader
import ai.deepsense.workflowexecutor.communication.mq.json.JsonMQSerializer

case class ProtocolJsonSerializer(graphReader: GraphReader)
    extends JsonMQSerializer(Seq(WorkflowProtocol.SynchronizeSerializer))
