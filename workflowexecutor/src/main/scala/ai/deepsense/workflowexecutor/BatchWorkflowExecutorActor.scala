package ai.deepsense.workflowexecutor

import akka.actor._

import ai.deepsense.commons.utils.Logging
import ai.deepsense.deeplang.CommonExecutionContext
import ai.deepsense.models.workflows.WorkflowWithResults
import ai.deepsense.workflowexecutor.partialexecution.Execution

class BatchWorkflowExecutorActor(
    executionContext: CommonExecutionContext,
    nodeExecutorFactory: GraphNodeExecutorFactory,
    terminationListener: ActorRef,
    initWorkflow: WorkflowWithResults
) extends WorkflowExecutorActor(
      executionContext,
      nodeExecutorFactory,
      None,
      None,
      Some(terminationListener),
      Execution.defaultExecutionFactory
    )
    with Actor
    with Logging {

  initWithWorkflow(initWorkflow)

  override def receive: Actor.Receive = ready()

}

object BatchWorkflowExecutorActor {

  def props(ec: CommonExecutionContext, statusListener: ActorRef, initWorkflow: WorkflowWithResults): Props =
    Props(new BatchWorkflowExecutorActor(ec, new GraphNodeExecutorFactoryImpl, statusListener, initWorkflow))

}
