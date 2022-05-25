package io.deepsense.workflowexecutor.executor

import scala.concurrent.Promise

import akka.actor.{Actor, Props}

import io.deepsense.sparkutils.AkkaUtils
import io.deepsense.models.workflows.ExecutionReport

class TerminationListenerActor(finishedExecutionStatus: Promise[ExecutionReport]) extends Actor {
  override def receive: Receive = {
    case status: ExecutionReport =>
      finishedExecutionStatus.success(status)
      AkkaUtils.terminate(context.system)
  }
}

object TerminationListenerActor {
  def props(finishedExecutionReport: Promise[ExecutionReport]): Props =
    Props(new TerminationListenerActor(finishedExecutionReport))
}
