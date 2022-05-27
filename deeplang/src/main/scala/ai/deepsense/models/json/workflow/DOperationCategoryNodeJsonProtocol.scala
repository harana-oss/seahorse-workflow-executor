package ai.deepsense.models.json.workflow

import spray.json._

import ai.deepsense.commons.json.IdJsonProtocol
import ai.deepsense.deeplang.catalogs.actions.ActionCategoryNode

trait ActionCategoryNodeJsonProtocol
    extends DefaultJsonProtocol
    with IdJsonProtocol
    with ActionDescriptorJsonProtocol {

  implicit object ActionCategoryNodeFormat extends RootJsonFormat[ActionCategoryNode] {

    implicit private val operationFormat = ActionDescriptorShortFormat

    override def write(obj: ActionCategoryNode): JsValue = {
      val fields    = Map("items" -> obj.operations.toJson, "catalog" -> obj.successors.values.toJson)
      val allFields = obj.category match {
        case Some(value) => fields ++ Map("id" -> obj.category.get.id.toJson, "name" -> obj.category.get.name.toJson)
        case None        => fields
      }
      JsObject(allFields)
    }

    override def read(json: JsValue): ActionCategoryNode =
      throw new UnsupportedOperationException

  }

}

object ActionCategoryNodeJsonProtocol extends ActionCategoryNodeJsonProtocol
