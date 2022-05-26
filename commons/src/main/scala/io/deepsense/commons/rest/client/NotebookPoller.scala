package io.deepsense.commons.rest.client

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.ExecutionContext
import scala.concurrent.Future

import akka.actor.ActorSystem
import akka.util.Timeout
import spray.client.pipelining._
import spray.http.StatusCodes

import io.deepsense.commons.models.Id
import io.deepsense.commons.utils.Retry
import io.deepsense.commons.utils.RetryActor.RetriableException

class NotebookPoller private (
    notebookRestClient: NotebookRestClient,
    pollInterval: FiniteDuration,
    retryCountLimit: Int,
    workflowId: Id,
    nodeId: Id,
    endpointPath: String
)(implicit override val actorSystem: ActorSystem, override val timeout: Timeout)
    extends Retry[Array[Byte]] {

  override val retryInterval: FiniteDuration = pollInterval

  override val retryLimit: Int = retryCountLimit

  override val workDescription: Option[String] = Some("notebook data retrieval")

  override def work: Future[Array[Byte]] = {
    implicit val ec: ExecutionContext = actorSystem.dispatcher

    notebookRestClient.fetchHttpResponse(Get(endpointPath)).flatMap { resp =>
      resp.status match {
        case StatusCodes.NotFound =>
          Future.failed(
            RetriableException(
              s"File containing output data for workflow " +
                s"s$workflowId and node s$nodeId not found",
              None
            )
          )
        case StatusCodes.OK =>
          Future.successful(resp.entity.data.toByteArray)
        case statusCode =>
          Future.failed(
            NotebookHttpException(
              resp,
              s"Notebook server responded with $statusCode " +
                s"when asked for file for workflow $workflowId and node $nodeId"
            )
          )
      }
    }
  }

}

object NotebookPoller {

  def apply(
      notebookRestClient: NotebookRestClient,
      pollInterval: FiniteDuration,
      retryCountLimit: Int,
      workflowId: Id,
      nodeId: Id,
      endpointPath: String
  )(implicit as: ActorSystem, tout: Timeout): Retry[Array[Byte]] = new NotebookPoller(
    notebookRestClient, pollInterval, retryCountLimit, workflowId, nodeId, endpointPath
  )

}
