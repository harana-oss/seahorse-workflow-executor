package io.deepsense.models.json.workflow

import spray.json._

import io.deepsense.commons.json.IdJsonProtocol
import io.deepsense.deeplang.catalogs.doperations.DOperationCategoryNode

trait DOperationCategoryNodeJsonProtocol
  extends DefaultJsonProtocol
  with IdJsonProtocol
  with DOperationDescriptorJsonProtocol {

  implicit object DOperationCategoryNodeFormat extends RootJsonFormat[DOperationCategoryNode] {
    private implicit val operationFormat = DOperationDescriptorShortFormat

    override def write(obj: DOperationCategoryNode): JsValue = {
      val fields = Map(
        "items" -> obj.operations.toJson,
        "catalog" -> obj.successors.values.toJson)
      val allFields = obj.category match {
        case Some(value) => fields ++ Map(
          "id" -> obj.category.get.id.toJson,
          "name" -> obj.category.get.name.toJson)
        case None => fields
      }
      JsObject(allFields)
    }

    override def read(json: JsValue): DOperationCategoryNode = {
      throw new UnsupportedOperationException
    }
  }
}

object DOperationCategoryNodeJsonProtocol extends DOperationCategoryNodeJsonProtocol
