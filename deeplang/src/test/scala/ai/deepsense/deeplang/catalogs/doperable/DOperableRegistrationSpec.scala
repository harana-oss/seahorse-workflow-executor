package ai.deepsense.deeplang.catalogs.doperable

import ai.deepsense.deeplang.CatalogRecorder
import ai.deepsense.deeplang.DOperable
import ai.deepsense.deeplang.UnitSpec

class DOperableRegistrationSpec extends UnitSpec {

  "DOperableCatalog" should {
    "successfully register and create all DOperables" in {
      val catalog = CatalogRecorder.resourcesCatalogRecorder.catalogs.operables
      catalog.concreteSubclassesInstances[DOperable]
    }
  }

}
