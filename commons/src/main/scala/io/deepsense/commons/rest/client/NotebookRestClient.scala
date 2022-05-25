package io.deepsense.commons.rest.client

import java.net.URL

import scala.concurrent._
import scala.concurrent.duration._
import scala.language.postfixOps

import akka.actor._
import akka.util.Timeout
import spray.client.pipelining._
import spray.http.{HttpResponse, StatusCodes}
import spray.httpx.SprayJsonSupport

import io.deepsense.commons.json.NotebookRestClientProtocol._
import io.deepsense.commons.models.Id
import io.deepsense.commons.rest.client.req.NotebookClientRequest
import io.deepsense.commons.utils.Logging

/**
  * Exception that will translate to an http error response with a specific
  * status code.
  */
case class NotebookHttpException(
    httpResponse: HttpResponse,
    msg: String,
    cause: Throwable = null)
  extends Exception(msg, cause)


class NotebookRestClient(
    notebooksServerAddress: URL,
    workflowId: Id,
    nodeId: Id,
    pollInterval: FiniteDuration,
    retryCountLimit: Int
)(implicit override val as: ActorSystem)
  extends Logging with RestClient with SprayJsonSupport {

  def apiUrl: java.net.URL = new URL(notebooksServerAddress, "/jupyter/")
  def credentials: Option[spray.http.HttpCredentials] = None
  def userId: Option[java.util.UUID] = None
  def userName: Option[String] = None

  implicit val timeout: Timeout = 70 minutes

  private val filenameExtension = "html"

  private val postPath = endpointPath("HeadlessNotebook")
  private val getPath = endpointPath(s"HeadlessNotebook/${workflowId}_$nodeId.$filenameExtension")

  private val poller = NotebookPoller(this, pollInterval, retryCountLimit, workflowId, nodeId, getPath)

  def pollForNotebookData(): Future[Array[Byte]] = poller.tryWork

  def generateNotebookData(language: String): Future[HttpResponse] = {
    val req = NotebookClientRequest(workflowId, nodeId, language)
    fetchHttpResponse(Post(postPath, req)).flatMap { resp => resp.status match {
      case StatusCodes.Success(_) => Future.successful(resp)
      case statusCode => Future.failed(NotebookHttpException(resp,
        s"Notebook server responded with $statusCode when asked to generate notebook data"
      ))
    }}
  }

  def generateAndPollNbData(language: String): Future[Array[Byte]] = {
    generateNotebookData(language).flatMap(_ => pollForNotebookData())
  }

  def toFactory: NotebooksClientFactory =
    new NotebooksClientFactory(notebooksServerAddress, pollInterval, retryCountLimit)

}

class NotebooksClientFactory(notebooksServerAddress: URL, pollInterval: FiniteDuration, retryCountLimit: Int)
  (implicit system: ActorSystem) {
  def createNotebookForNode(workflow: Id, node: Id): NotebookRestClient = {
    new NotebookRestClient(notebooksServerAddress, workflow, node, pollInterval, retryCountLimit)
  }
}
