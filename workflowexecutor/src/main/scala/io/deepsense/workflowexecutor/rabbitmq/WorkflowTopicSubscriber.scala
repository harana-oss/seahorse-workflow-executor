package io.deepsense.workflowexecutor.rabbitmq

import akka.actor._

import io.deepsense.commons.utils.Logging
import io.deepsense.models.workflows.Workflow
import io.deepsense.sparkutils.AkkaUtils
import io.deepsense.workflowexecutor.communication.message.global.Launch
import io.deepsense.workflowexecutor.communication.message.{global, workflow}
import io.deepsense.workflowexecutor.executor.Executor
import io.deepsense.workflowexecutor.{SessionWorkflowExecutorActorProvider, WorkflowExecutorActor}

/**
  * Handles messages with topic workflow.&#36;{id}. All messages directed to workflows.
  */
case class WorkflowTopicSubscriber(
      actorProvider: SessionWorkflowExecutorActorProvider,
      sessionId: String,
      workflowId: Workflow.Id)
    extends Actor
    with Logging
    with Executor {

  private val executorActor: ActorRef = actorProvider.provide(context, workflowId)

  override def receive: Receive = {
    case WorkflowExecutorActor.Messages.Init() =>
      logger.debug(s"Initializing SessionWorkflowExecutorActor for workflow '$workflowId'")
      executorActor ! WorkflowExecutorActor.Messages.Init()
    case global.Launch(id, nodesToExecute) if id == workflowId =>
      logger.debug(s"LAUNCH! '$workflowId'")
      executorActor ! WorkflowExecutorActor.Messages.Launch(nodesToExecute)
    case workflow.Abort(id) if id == workflowId =>
      logger.debug(s"ABORT! '$workflowId'")
      executorActor ! WorkflowExecutorActor.Messages.Abort()
    case workflow.UpdateWorkflow(id, workflow) if id == workflowId =>
      logger.debug(s"UPDATE STRUCT '$workflowId'")
      executorActor ! WorkflowExecutorActor.Messages.UpdateStruct(workflow)
    case workflow.Synchronize() =>
      logger.debug(s"Got Synchronize() request for workflow '$workflowId'")
      executorActor ! WorkflowExecutorActor.Messages.Synchronize()
    case global.PoisonPill() =>
      logger.info("Got PoisonPill! Terminating Actor System!")
      AkkaUtils.terminate(context.system)
    case x =>
      logger.error(s"Unexpected '$x' from '${sender()}'!")
  }
}

object WorkflowTopicSubscriber {
  def props(
      actorProvider: SessionWorkflowExecutorActorProvider,
      sessionId: String,
      workflowId: Workflow.Id): Props = {
    Props(WorkflowTopicSubscriber(
      actorProvider,
      sessionId,
      workflowId))
  }
}
