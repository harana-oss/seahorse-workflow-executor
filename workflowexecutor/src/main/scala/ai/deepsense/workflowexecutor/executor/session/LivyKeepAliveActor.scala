package ai.deepsense.workflowexecutor.executor.session

import scala.concurrent.duration._

import akka.actor.Actor
import akka.actor.Props

import ai.deepsense.commons.utils.Logging

/** Livy reads process output to determine whether the process is working properly. If a process does not log anything
  * for a certain, configured period of time (default: 1 hour), then it is considered a broken process and closed. This
  * Actor periodically generates output, so that Livy does not kill Session Executor.
  *
  * If Livy can be configured not to kill long-living batch jobs that do not produce output, then this Actor is no
  * longer useful.
  */
class LivyKeepAliveActor(private val interval: FiniteDuration) extends Actor with Logging {

  import context.dispatcher

  context.system.scheduler.schedule(interval, interval, self, Tick)

  override def receive: Receive = { case Tick() =>
    logger.info("This is a keep-alive message!")
  }

  private case class Tick()

}

object LivyKeepAliveActor {

  def props(interval: FiniteDuration): Props =
    Props(new LivyKeepAliveActor(interval))

}
