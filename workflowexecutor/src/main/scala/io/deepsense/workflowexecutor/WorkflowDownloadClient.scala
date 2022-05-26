package io.deepsense.workflowexecutor

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.Try

import akka.actor.ActorSystem
import akka.io.IO
import akka.pattern.ask
import spray.can.Http
import spray.client.pipelining._
import spray.http._
import spray.util._

import io.deepsense.commons.utils.Logging
import io.deepsense.sparkutils
import io.deepsense.workflowexecutor.exception.UnexpectedHttpResponseException

class WorkflowDownloadClient(val address: String, val path: String, val timeout: Int) extends Logging {

  val downloadUrl = (workflowId: String) => s"$address/$path/$workflowId/download"

  def downloadWorkflow(workflowId: String): Future[String] = {

    logger.info(s"Downloading workflow $workflowId...")

    implicit val system = ActorSystem()
    import system.dispatcher
    implicit val timeoutSeconds = timeout.seconds

    val pipeline: HttpRequest => Future[HttpResponse] = sendReceive
    val futureResponse                                = pipeline(Get(downloadUrl(workflowId)))

    futureResponse.onComplete { _ =>
      Try(IO(Http).ask(Http.CloseAll)(1.second).await)
      sparkutils.AkkaUtils.terminate(system)
    }
    futureResponse.map(handleResponse)
  }

  private def handleResponse(response: HttpResponse): String =
    response.status match {
      case StatusCodes.OK =>
        response.entity.data.asString
      case _ =>
        throw UnexpectedHttpResponseException(
          "Workflow download failed",
          response.status,
          response.entity.data.asString
        )
    }

}
