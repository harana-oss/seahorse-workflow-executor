package io.deepsense.workflowexecutor

import scala.concurrent.duration.FiniteDuration

import akka.actor.{ActorContext, ActorRef}

import io.deepsense.commons.utils.Logging
import io.deepsense.deeplang.CommonExecutionContext
import io.deepsense.models.workflows.Workflow

class SessionWorkflowExecutorActorProvider(
  executionContext: CommonExecutionContext,
  workflowManagerClientActor: ActorRef,
  heartbeatPublisher: ActorRef,
  notebookTopicPublisher: ActorRef,
  workflowManagerTimeout: Int,
  publisher: ActorRef,
  sessionId: String,
  heartbeatInterval: FiniteDuration
) extends Logging {
  def provide(context: ActorContext, workflowId: Workflow.Id): ActorRef = {
    context.actorOf(
      SessionWorkflowExecutorActor.props(
        executionContext,
        workflowManagerClientActor,
        publisher,
        heartbeatPublisher,
        notebookTopicPublisher,
        workflowManagerTimeout,
        sessionId,
        heartbeatInterval),
      workflowId.toString)
  }
}
