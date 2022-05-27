package ai.deepsense.models.json.graph

import ai.deepsense.commons.json.IdJsonProtocol
import ai.deepsense.deeplang.Action
import ai.deepsense.deeplang.catalogs.actions.ActionCatalog
import ai.deepsense.deeplang.catalogs.actions.exceptions.DOperationNotFoundException
import spray.json._

import ai.deepsense.models.json.graph.GraphJsonProtocol.GraphReader

object OperationJsonProtocol extends IdJsonProtocol {

  val Operation = "operation"

  val Name = "name"

  val Id = "id"

  val Parameters = "parameters"

  implicit object DOperationWriter extends JsonWriter[Action] with DefaultJsonProtocol with IdJsonProtocol {

    override def write(operation: Action): JsValue = {
      JsObject(
        Operation  -> JsObject(Id -> operation.id.toJson, Name -> operation.name.toJson),
        Parameters -> operation.paramValuesToJson
      )
    }

  }

  class DOperationReader(graphReader: GraphReader) extends JsonReader[Action] with DefaultJsonProtocol {

    override def read(json: JsValue): Action = json match {
      case JsObject(fields) =>
        val operationJs = fields(Operation).asJsObject
        val operationId = operationJs.fields
          .getOrElse(Id, deserializationError(s"Operation id field '$Id' is missing"))
          .convertTo[Action.Id]

        val operation =
          try
            graphReader.catalog.createDOperation(operationId)
          catch {
            case notFound: DOperationNotFoundException =>
              deserializationError(s"DOperation with id = '${notFound.operationId}' does not exist", notFound)
          }

        val parameters =
          fields.getOrElse(Parameters, deserializationError(s"Operation parameters field '$Parameters' is missing"))

        operation.setParamsFromJson(parameters, graphReader)
        operation
      case x                =>
        throw new DeserializationException(s"Expected JsObject with a node but got $x")
    }

  }

}
