package ai.deepsense.commons.utils

import scala.concurrent.Await
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.language.postfixOps

import akka.actor.ActorSystem
import akka.util.Timeout

import ai.deepsense.commons.utils.RetryActor.RetriableException
import ai.deepsense.commons.utils.RetryActor.RetryLimitReachedException
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class RetrySpec extends AnyWordSpec with Matchers {

  val uutName = classOf[Retry[_]].getSimpleName.filterNot(_ == '$')

  trait Setup {

    def generateUUT[T](retryLimitCount: Int)(toDo: => Future[T]): Retry[T] = new {
      override val workDescription = Some("test work")

      override val actorSystem: ActorSystem = ActorSystem()

      override val retryInterval = 1 nano

      override val retryLimit = retryLimitCount

      override val timeout = Timeout(1 minute)

    } with Retry[T] {

      override def work: Future[T] = toDo

    }

  }

  s"A $uutName" should {
    "complete its work" when {
      "no exceptions are thrown" in {
        new Setup {
          val uut = generateUUT(0) {
            Future.successful(2 * 3 + 8)
          }

          Await.result(uut.tryWork, Duration.Inf) shouldBe 14
        }
      }

      "only retriable exceptions are thrown and retry limit is not reached" in {
        new Setup {
          var count = 3
          val uut   = generateUUT(3) {
            if (count > 0) {
              count -= 1
              Future.failed(RetriableException(s"Thrown because count is ${count + 1}", None))
            } else
              Future.successful("success")
          }

          Await.result(
            uut.tryWork,
            Duration.Inf
          ) shouldBe "success"

          count shouldBe 0
        }
      }
    }

    "fail" when {
      "retry limit is reached" in {
        new Setup {
          val uut = generateUUT(10) {
            Future.failed(RetriableException(s"This will never succeed, yet we keep trying", None))
          }

          a[RetryLimitReachedException] shouldBe thrownBy(Await.result(uut.tryWork, Duration.Inf))

        }
      }

      "unexpected exception is thrown" in {
        var count = 1
        new Setup {
          val uut = generateUUT(10) {
            if (count == 0)
              Future.failed(new RuntimeException("Thrown because counter reached zero"))
            else {
              count -= 1
              Future.failed(RetriableException(s"Thrown because counter was ${count + 1}", None))
            }
          }

          a[RuntimeException] shouldBe thrownBy(Await.result(uut.tryWork, Duration.Inf))
          count shouldBe 0
        }
      }
    }
  }

}
