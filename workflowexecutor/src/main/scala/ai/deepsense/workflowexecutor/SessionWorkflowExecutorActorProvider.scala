package ai.deepsense.workflowexecutor

import scala.concurrent.duration.FiniteDuration

import akka.actor.ActorContext
import akka.actor.ActorRef

import ai.deepsense.commons.utils.Logging
import ai.deepsense.deeplang.CommonExecutionContext
import ai.deepsense.models.workflows.Workflow

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
      SessionWorkflowExecutorActor.props(executionContext, workflowManagerClientActor, publisher, heartbeatPublisher,
        notebookTopicPublisher, workflowManagerTimeout, sessionId, heartbeatInterval),
      workflowId.toString
    )
  }

}
