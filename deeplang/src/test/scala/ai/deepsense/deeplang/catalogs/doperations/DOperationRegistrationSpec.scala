package ai.deepsense.deeplang.catalogs.doperations

import ai.deepsense.deeplang.DOperationCategories.UserDefined
import ai.deepsense.deeplang.CatalogRecorder
import ai.deepsense.deeplang.UnitSpec

class DOperationRegistrationSpec extends UnitSpec {

  "DOperationsCatalog" should {
    val catalogs   = CatalogRecorder.resourcesCatalogRecorder.catalogs
    val operations = catalogs.operations
    "successfully register and create all DOperations" in {
      operations.operations.keys.foreach(id => operations.createDOperation(id))
    }
    "report assigned categories" in {
      val delta = catalogs.categories.diff(operations.categories)
      delta shouldBe empty
    }
  }

}
