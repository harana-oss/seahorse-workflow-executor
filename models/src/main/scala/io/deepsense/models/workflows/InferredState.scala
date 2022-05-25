package io.deepsense.models.workflows

import io.deepsense.graph.GraphKnowledge

case class InferredState(
  id: Workflow.Id,
  graphKnowledge: GraphKnowledge,
  states: ExecutionReport)
