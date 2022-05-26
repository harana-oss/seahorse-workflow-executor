package io.deepsense.deeplang.catalogs.doperable

import io.deepsense.deeplang.CatalogRecorder
import io.deepsense.deeplang.DOperable
import io.deepsense.deeplang.UnitSpec

class DOperableRegistrationSpec extends UnitSpec {

  "DOperableCatalog" should {
    "successfully register and create all DOperables" in {
      val catalog = CatalogRecorder.resourcesCatalogRecorder.catalogs.dOperableCatalog
      catalog.concreteSubclassesInstances[DOperable]
    }
  }

}
