package ai.deepsense.commons.rest.client

import java.net.URL
import java.util.UUID

import scala.concurrent.ExecutionContext
import scala.concurrent.Future

import akka.io.IO
import akka.pattern.ask
import spray.can.Http
import spray.can.Http.HostConnectorInfo
import spray.client.pipelining._
import spray.http.HttpCredentials
import spray.http.HttpRequest
import spray.http.HttpResponse
import spray.httpx.unmarshalling.FromResponseUnmarshaller

trait RestClient extends RestClientImplicits {

  import RestClient._

  def apiUrl: URL

  def userId: Option[UUID]

  def userName: Option[String]

  def credentials: Option[HttpCredentials]

  implicit override val ctx: ExecutionContext = as.dispatcher

  private def hostConnectorFut(): Future[HostConnectorInfo] =
    (IO(Http) ? Http.HostConnectorSetup(apiUrl.getHost, port = apiUrl.getPort)).mapTo[HostConnectorInfo]

  private def sendReceivePipeline(): Future[HttpRequest => Future[HttpResponse]] = {
    for {
      HostConnectorInfo(hostConnector, _) <- hostConnectorFut()
    } yield {
      credentials.map(addCredentials).getOrElse[RequestTransformer](identity) ~>
        userId.map(id => addHeader(UserIdHeader, id.toString)).getOrElse[RequestTransformer](identity) ~>
        userName.map(addHeader(UserNameHeader, _)).getOrElse[RequestTransformer](identity) ~>
        sendReceive(hostConnector)
    }
  }

  private def unmarshalPipeline[U: FromResponseUnmarshaller](): Future[HttpRequest => Future[U]] = {
    for {
      sr <- sendReceivePipeline()
    } yield sr ~> unmarshal[U]
  }

  def fetchResponse[U: FromResponseUnmarshaller](
      req: HttpRequest
  ): Future[U] =
    unmarshalPipeline().flatMap(_(req))

  def fetchHttpResponse(req: HttpRequest): Future[HttpResponse] =
    sendReceivePipeline().flatMap(_(req))

  def endpointPath(endpoint: String): String =
    new URL(apiUrl, endpoint).getFile

}

object RestClient {

  val UserIdHeader = "X-Seahorse-UserId"

  val UserNameHeader = "X-Seahorse-UserName"

}
