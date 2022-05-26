package ai.deepsense.commons.utils

import scala.concurrent.Future
import scala.concurrent.duration.FiniteDuration

import akka.actor.ActorSystem
import akka.actor.Props
import akka.pattern.ask
import akka.util.Timeout

trait Retry[T] {

  def work: Future[T]

  def retryInterval: FiniteDuration

  def retryLimit: Int

  def actorSystem: ActorSystem

  // the timeout should exceed the retryLimit * retryInterval + (retryLimit + 1) * avgWorkDuration
  // otherwise the ask in tryWork method may timeout before all the retries have been attempted
  implicit def timeout: Timeout

  def workDescription: Option[String]

  private lazy val retryActor = actorSystem.actorOf(
    Props(
      new RetryActor[T](
        retryInterval,
        retryLimit,
        work,
        workDescription
      )
    )
  )

  def tryWork: Future[T] = (retryActor ? RetryActor.Trigger).asInstanceOf[Future[T]]

}
