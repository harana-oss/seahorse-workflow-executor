package ai.deepsense.models.json.workflow

import spray.json._

import ai.deepsense.commons.json.IdJsonProtocol
import ai.deepsense.commons.models.Entity
import ai.deepsense.models.workflows.EntitiesMap
import ai.deepsense.reportlib.model.ReportJsonProtocol

trait EntitiesMapJsonProtocol extends IdJsonProtocol {

  import ReportJsonProtocol._

  implicit val entitiesMapEntryFormat = jsonFormat2(EntitiesMap.Entry)

  implicit val entitiesMapFormat = new JsonFormat[EntitiesMap] {

    override def write(obj: EntitiesMap): JsValue =
      obj.entities.toJson

    override def read(json: JsValue): EntitiesMap = {
      val jsObject = json.asJsObject
      val entities = jsObject.fields.map { case (key, value) =>
        val id    = Entity.Id.fromString(key)
        val entry = value.convertTo[EntitiesMap.Entry]
        (id, entry)
      }
      EntitiesMap(entities)
    }

  }

}

object EntitiesMapJsonProtocol extends EntitiesMapJsonProtocol
