package ai.deepsense.deeplang.catalogs

import ai.deepsense.deeplang.catalogs.FlowCatalog.DCategoryCatalog
import ai.deepsense.deeplang.catalogs.actionobjects.ActionObjectCatalog
import ai.deepsense.deeplang.catalogs.actions.ActionCategory
import ai.deepsense.deeplang.catalogs.actions.ActionCatalog

class FlowCatalog(val categories: DCategoryCatalog, val operables: ActionObjectCatalog, val operations: ActionCatalog)

object FlowCatalog {

  type DCategoryCatalog = Seq[ActionCategory]

  def apply(categories: DCategoryCatalog, operables: ActionObjectCatalog, operations: ActionCatalog) =
    new FlowCatalog(categories, operables, operations)

  def apply(operables: ActionObjectCatalog, operations: ActionCatalog) =
    new FlowCatalog(operations.categories, operables, operations)

  def unapply(cat: FlowCatalog): Option[(DCategoryCatalog, ActionObjectCatalog, ActionCatalog)] =
    Some((cat.categories, cat.operables, cat.operations))

}
