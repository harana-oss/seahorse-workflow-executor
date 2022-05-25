package io.deepsense.deeplang.catalogs.doperable

import io.deepsense.deeplang.{CatalogRecorder, DOperable, UnitSpec}

class DOperableRegistrationSpec extends UnitSpec {

  "DOperableCatalog" should {
    "successfully register and create all DOperables" in {
      val catalog = CatalogRecorder.resourcesCatalogRecorder.catalogs.dOperableCatalog
      catalog.concreteSubclassesInstances[DOperable]
    }
  }
}
