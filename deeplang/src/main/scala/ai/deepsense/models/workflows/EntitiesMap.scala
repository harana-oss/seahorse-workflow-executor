package ai.deepsense.models.workflows

import ai.deepsense.commons.models.Entity
import ai.deepsense.deeplang.ActionObject
import ai.deepsense.reportlib.model.ReportContent

/** Wraps a map of entities created during workflow execution. It maps an entity id into a pair of its class name and
  * report.
  */
case class EntitiesMap(entities: Map[Entity.Id, EntitiesMap.Entry] = Map()) {

  def subMap(keys: Set[Entity.Id]): EntitiesMap =
    EntitiesMap(keys.intersect(entities.keySet).map(key => key -> entities(key)).toMap)

}

object EntitiesMap {

  case class Entry(className: String, report: Option[ReportContent] = None)

  def apply(results: Map[Entity.Id, ActionObject], reports: Map[Entity.Id, ReportContent]): EntitiesMap = {
    EntitiesMap(results.map { case (id, entity) =>
      val entry = EntitiesMap.Entry(entity.getClass.getCanonicalName, reports.get(id))
      (id, entry)
    })
  }

}
