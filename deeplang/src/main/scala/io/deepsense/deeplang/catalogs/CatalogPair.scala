package io.deepsense.deeplang.catalogs

import io.deepsense.deeplang.catalogs.doperable.DOperableCatalog
import io.deepsense.deeplang.catalogs.doperations.DOperationsCatalog

case class CatalogPair(dOperableCatalog: DOperableCatalog, dOperationsCatalog: DOperationsCatalog)
