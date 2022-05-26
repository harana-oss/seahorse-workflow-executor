package ai.deepsense.models.json.workflow

import java.util.NoSuchElementException

import spray.json._

import ai.deepsense.commons.exception.FailureDescription
import ai.deepsense.graph.Node
import ai.deepsense.graph.nodestate.NodeStatus
import ai.deepsense.models.json.graph.NodeStatusJsonProtocol
import ai.deepsense.models.workflows._

trait ExecutionReportJsonProtocol extends NodeStatusJsonProtocol with EntitiesMapJsonProtocol {

  implicit val executionReportJsonFormat: RootJsonFormat[ExecutionReport] =
    new RootJsonFormat[ExecutionReport] {

      override def write(executionReport: ExecutionReport): JsValue = JsObject(
        "resultEntities" -> executionReport.resultEntities.toJson,
        "nodes"          -> executionReport.nodesStatuses.toJson,
        "error"          -> executionReport.error.toJson
      )

      override def read(json: JsValue): ExecutionReport = {
        val fieldGetter                       = getField(json.asJsObject.fields) _
        val resultEntities: EntitiesMap       = fieldGetter("resultEntities").convertTo[EntitiesMap]
        val nodes: Map[Node.Id, NodeStatus]   = fieldGetter("nodes").convertTo[Map[Node.Id, NodeStatus]]
        val error: Option[FailureDescription] =
          fieldGetter("error").convertTo[Option[FailureDescription]]
        ExecutionReport(nodes, resultEntities, error)
      }

    }

  private def getField(fields: Map[String, JsValue])(fieldName: String): JsValue = {
    try
      fields(fieldName)
    catch {
      case e: NoSuchElementException =>
        throw new DeserializationException(s"Could not find field: $fieldName", e)
    }
  }

}

object ExecutionReportJsonProtocol extends ExecutionReportJsonProtocol
