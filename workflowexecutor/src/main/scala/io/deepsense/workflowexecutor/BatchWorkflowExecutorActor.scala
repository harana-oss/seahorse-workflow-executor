package io.deepsense.workflowexecutor

import akka.actor._

import io.deepsense.commons.utils.Logging
import io.deepsense.deeplang.CommonExecutionContext
import io.deepsense.models.workflows.WorkflowWithResults
import io.deepsense.workflowexecutor.partialexecution.Execution

class BatchWorkflowExecutorActor(
    executionContext: CommonExecutionContext,
    nodeExecutorFactory: GraphNodeExecutorFactory,
    terminationListener: ActorRef,
    initWorkflow: WorkflowWithResults)
  extends WorkflowExecutorActor(
    executionContext,
    nodeExecutorFactory,
    None,
    None,
    Some(terminationListener),
    Execution.defaultExecutionFactory)
  with Actor
  with Logging {

  initWithWorkflow(initWorkflow)

  override def receive: Actor.Receive = ready()
}

object BatchWorkflowExecutorActor {
  def props(
    ec: CommonExecutionContext,
    statusListener: ActorRef,
    initWorkflow: WorkflowWithResults): Props =
    Props(new BatchWorkflowExecutorActor(
      ec,
      new GraphNodeExecutorFactoryImpl,
      statusListener,
      initWorkflow))
}
