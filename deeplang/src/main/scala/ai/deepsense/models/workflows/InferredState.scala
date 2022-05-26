package ai.deepsense.models.workflows

import ai.deepsense.graph.GraphKnowledge

case class InferredState(id: Workflow.Id, graphKnowledge: GraphKnowledge, states: ExecutionReport)
