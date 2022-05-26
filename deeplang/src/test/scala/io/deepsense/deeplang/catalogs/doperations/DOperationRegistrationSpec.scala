package io.deepsense.deeplang.catalogs.doperations

import io.deepsense.deeplang.CatalogRecorder
import io.deepsense.deeplang.UnitSpec

class DOperationRegistrationSpec extends UnitSpec {

  "DOperationsCatalog" should {
    "successfully register and create all DOperations" in {
      val catalog = CatalogRecorder.resourcesCatalogRecorder.catalogs.dOperationsCatalog
      catalog.operations.keys.foreach(id => catalog.createDOperation(id))
    }
  }

}
