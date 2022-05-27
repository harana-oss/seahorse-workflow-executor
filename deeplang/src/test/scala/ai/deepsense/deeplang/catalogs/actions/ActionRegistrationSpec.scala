package ai.deepsense.deeplang.catalogs.actions

import ai.deepsense.deeplang.ActionCategories.UserDefined
import ai.deepsense.deeplang.CatalogRecorder
import ai.deepsense.deeplang.UnitSpec

class ActionRegistrationSpec extends UnitSpec {

  "ActionsCatalog" should {
    val catalogs   = CatalogRecorder.resourcesCatalogRecorder.catalogs
    val operations = catalogs.operations
    "successfully register and create all Actions" in {
      operations.operations.keys.foreach(id => operations.createDOperation(id))
    }
    "report assigned categories" in {
      val delta = catalogs.categories.diff(operations.categories)
      delta shouldBe empty
    }
  }

}
