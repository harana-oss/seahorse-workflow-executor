package ai.deepsense.workflowexecutor.executor

import scala.concurrent.Promise

import akka.actor.Actor
import akka.actor.Props

import ai.deepsense.sparkutils.AkkaUtils
import ai.deepsense.models.workflows.ExecutionReport

class TerminationListenerActor(finishedExecutionStatus: Promise[ExecutionReport]) extends Actor {

  override def receive: Receive = { case status: ExecutionReport =>
    finishedExecutionStatus.success(status)
    AkkaUtils.terminate(context.system)
  }

}

object TerminationListenerActor {

  def props(finishedExecutionReport: Promise[ExecutionReport]): Props =
    Props(new TerminationListenerActor(finishedExecutionReport))

}
