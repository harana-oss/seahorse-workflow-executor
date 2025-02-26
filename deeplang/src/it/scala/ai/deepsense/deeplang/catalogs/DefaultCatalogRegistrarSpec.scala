package ai.deepsense.deeplang.catalogs

import ai.deepsense.deeplang.catalogs.spi.CatalogRegistrant
import ai.deepsense.deeplang.Action
import ai.deepsense.deeplang.UnitSpec
import ai.deepsense.deeplang.catalogs.spi.CatalogRegistrar.DefaultCatalogRegistrar
import org.scalatest.BeforeAndAfter

class DefaultCatalogRegistrarSpec extends UnitSpec with BeforeAndAfter {

  val defaultCatalogRegistrar = new DefaultCatalogRegistrar()

  val operationCatalog = defaultCatalogRegistrar.catalog.operations

  CatalogRegistrant.load(defaultCatalogRegistrar, null)
  "Default Catalog Registrar" should {
    "contain operation loaded using test registator" in {
      operationCatalog.operations.keys should contain(SpiLoadedOperation.spiLoadedOperationId)
    }
    "create operation loaded using test registator" in {
      val operation = operationCatalog.createAction(SpiLoadedOperation.spiLoadedOperationUuid)
      operation.id shouldBe SpiLoadedOperation.spiLoadedOperationId
    }
  }

}
