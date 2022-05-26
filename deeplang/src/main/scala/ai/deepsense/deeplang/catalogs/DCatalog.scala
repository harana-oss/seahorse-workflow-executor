package ai.deepsense.deeplang.catalogs

import ai.deepsense.deeplang.catalogs.DCatalog.DCategoryCatalog
import ai.deepsense.deeplang.catalogs.doperable.DOperableCatalog
import ai.deepsense.deeplang.catalogs.doperations.DOperationCategory
import ai.deepsense.deeplang.catalogs.doperations.DOperationsCatalog

class DCatalog(val categories: DCategoryCatalog, val operables: DOperableCatalog, val operations: DOperationsCatalog)

object DCatalog {

  type DCategoryCatalog = Seq[DOperationCategory]

  def apply(categories: DCategoryCatalog, operables: DOperableCatalog, operations: DOperationsCatalog) =
    new DCatalog(categories, operables, operations)

  def apply(operables: DOperableCatalog, operations: DOperationsCatalog) =
    new DCatalog(operations.categories, operables, operations)

  def unapply(cat: DCatalog): Option[(DCategoryCatalog, DOperableCatalog, DOperationsCatalog)] =
    Some((cat.categories, cat.operables, cat.operations))

}
