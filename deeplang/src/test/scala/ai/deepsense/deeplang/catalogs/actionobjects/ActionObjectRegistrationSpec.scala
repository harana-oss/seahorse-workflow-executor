package ai.deepsense.deeplang.catalogs.actionobjects

import ai.deepsense.deeplang.CatalogRecorder
import ai.deepsense.deeplang.ActionObject
import ai.deepsense.deeplang.UnitSpec

class ActionObjectRegistrationSpec extends UnitSpec {

  "ActionObjectCatalog" should {
    "successfully register and create all ActionObjects" in {
      val catalog = CatalogRecorder.resourcesCatalogRecorder.catalogs.operables
      catalog.concreteSubclassesInstances[ActionObject]
    }
  }

}
