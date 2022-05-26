package io.deepsense.models.json.graph

import io.deepsense.commons.json.IdJsonProtocol
import io.deepsense.deeplang.DOperation
import io.deepsense.deeplang.catalogs.doperations.DOperationsCatalog
import io.deepsense.deeplang.catalogs.doperations.exceptions.DOperationNotFoundException
import spray.json._

object OperationJsonProtocol extends IdJsonProtocol {

  val Operation = "operation"

  val Name = "name"

  val Id = "id"

  val Parameters = "parameters"

  implicit object DOperationWriter extends JsonWriter[DOperation] with DefaultJsonProtocol with IdJsonProtocol {

    override def write(operation: DOperation): JsValue =
      JsObject(
        Operation  -> JsObject(Id -> operation.id.toJson, Name -> operation.name.toJson),
        Parameters -> operation.paramValuesToJson
      )

  }

  class DOperationReader(catalog: DOperationsCatalog) extends JsonReader[DOperation] with DefaultJsonProtocol {

    override def read(json: JsValue): DOperation = json match {
      case JsObject(fields) =>
        val operationJs = fields(Operation).asJsObject
        val operationId = operationJs.fields
          .getOrElse(Id, deserializationError(s"Operation id field '$Id' is missing"))
          .convertTo[DOperation.Id]

        val operation =
          try
            catalog.createDOperation(operationId)
          catch {
            case notFound: DOperationNotFoundException =>
              deserializationError(s"DOperation with id = '${notFound.operationId}' does not exist", notFound)
          }

        val parameters =
          fields.getOrElse(Parameters, deserializationError(s"Operation parameters field '$Parameters' is missing"))

        operation.setParamsFromJson(parameters)
        operation
      case x =>
        throw new DeserializationException(s"Expected JsObject with a node but got $x")
    }

  }

}
