package io.deepsense.workflowexecutor

import scala.concurrent.Future

import akka.actor.Actor
import akka.actor.Props
import akka.pattern.pipe
import spray.client.pipelining._
import spray.http.BasicHttpCredentials
import spray.http.HttpRequest
import spray.http.HttpResponse
import spray.http.StatusCodes
import spray.json._

import io.deepsense.commons.utils.Logging
import io.deepsense.models.json.graph.GraphJsonProtocol.GraphReader
import io.deepsense.models.json.workflow.WorkflowWithResultsJsonProtocol
import io.deepsense.models.workflows.ExecutionReport
import io.deepsense.models.workflows.Workflow
import io.deepsense.models.workflows.WorkflowWithResults
import io.deepsense.workflowexecutor.WorkflowManagerClientActorProtocol.GetWorkflow
import io.deepsense.workflowexecutor.WorkflowManagerClientActorProtocol.Request
import io.deepsense.workflowexecutor.WorkflowManagerClientActorProtocol.SaveState
import io.deepsense.workflowexecutor.WorkflowManagerClientActorProtocol.SaveWorkflow
import io.deepsense.workflowexecutor.exception.UnexpectedHttpResponseException

class WorkflowManagerClientActor(
    val workflowOwnerId: String,
    val wmUsername: String,
    val wmPassword: String,
    val workflowApiAddress: String,
    val workflowApiPrefix: String,
    val reportsApiPrefix: String,
    override val graphReader: GraphReader
) extends Actor
    with WorkflowWithResultsJsonProtocol
    with Logging {

  import context.dispatcher

  private val SeahorseUserIdHeaderName = "X-Seahorse-UserId"

  override def receive: Receive = {
    case r: Request =>
      r match {
        case GetWorkflow(workflowId)      => getWorkflow(workflowId).pipeTo(sender())
        case SaveWorkflow(workflow)       => saveWorkflowWithState(workflow).pipeTo(sender())
        case SaveState(workflowId, state) => saveState(workflowId, state).pipeTo(sender())
      }
    case message => unhandled(message)
  }

  private val downloadWorkflowUrl = (workflowId: Workflow.Id) => s"$workflowApiAddress/$workflowApiPrefix/$workflowId"

  private val saveWorkflowWithStateUrl = (workflowId: Workflow.Id) =>
    s"$workflowApiAddress/$workflowApiPrefix/$workflowId"

  private val saveStateUrl = (workflowId: Workflow.Id) => s"$workflowApiAddress/$reportsApiPrefix/$workflowId"

  private def getWorkflow(workflowId: Workflow.Id): Future[Option[WorkflowWithResults]] = {
    val url: String = downloadWorkflowUrl(workflowId)
    logger.debug("GET workflow URL: {}", url)
    pipeline(Get(url)).map(handleGetResponse)
  }

  private def saveWorkflowWithState(workflow: WorkflowWithResults): Future[Unit] =
    pipeline(Put(saveWorkflowWithStateUrl(workflow.id), workflow))
      .map(handleUploadResponse)

  private def saveState(workflowId: Workflow.Id, state: ExecutionReport): Future[Unit] =
    pipeline(Put(saveStateUrl(workflowId), state))
      .map(handleUploadResponse)

  private def handleGetResponse(response: HttpResponse): Option[WorkflowWithResults] =
    response.status match {
      case StatusCodes.OK =>
        Some(response.entity.data.asString.parseJson.convertTo[WorkflowWithResults])
      case StatusCodes.NotFound =>
        None
      case _ =>
        throw UnexpectedHttpResponseException(
          "Workflow download failed",
          response.status,
          response.entity.data.asString
        )
    }

  private def handleUploadResponse(response: HttpResponse): Unit =
    response.status match {
      case success: StatusCodes.Success => ()
      case _                            => throw UnexpectedHttpResponseException("Upload failed", response.status, response.entity.data.asString)
    }

  private val addUserIdHeader: HttpRequest => HttpRequest =
    _ ~> addHeader(SeahorseUserIdHeaderName, workflowOwnerId)

  private val addWMCredentials: HttpRequest => HttpRequest =
    _ ~> addCredentials(BasicHttpCredentials(wmUsername, wmPassword))

  private val pipeline: HttpRequest => Future[HttpResponse] =
    addUserIdHeader.andThen(addWMCredentials).andThen(sendReceive)

}

object WorkflowManagerClientActor {

  def props(
      workflowOwnerId: String,
      wmUsername: String,
      wmPassword: String,
      workflowApiAddress: String,
      workflowApiPrefix: String,
      reportsApiPrefix: String,
      graphReader: GraphReader
  ): Props =
    Props(
      new WorkflowManagerClientActor(
        workflowOwnerId,
        wmUsername: String,
        wmPassword: String,
        workflowApiAddress,
        workflowApiPrefix,
        reportsApiPrefix,
        graphReader
      )
    )

}

object WorkflowManagerClientActorProtocol {

  sealed trait Request

  case class GetWorkflow(workflowId: Workflow.Id) extends Request

  case class SaveWorkflow(workflow: WorkflowWithResults) extends Request

  case class SaveState(workflowId: Workflow.Id, state: ExecutionReport) extends Request

}
