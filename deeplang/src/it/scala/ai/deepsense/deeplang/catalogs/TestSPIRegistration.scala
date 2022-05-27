package ai.deepsense.deeplang.catalogs

import scala.reflect.runtime.{universe => ru}

import ai.deepsense.deeplang.Action.Id
import ai.deepsense.deeplang.Action
import ai.deepsense.deeplang.Action0To1
import ai.deepsense.deeplang.ActionCategories
import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.catalogs.spi.CatalogRegistrant
import ai.deepsense.deeplang.catalogs.spi.CatalogRegistrar
import ai.deepsense.deeplang.parameters.Parameter
import ai.deepsense.graph.DClassesForActions.A1

object SpiLoadedOperation {

  val spiLoadedOperationUuid = "adf440aa-d3eb-4cb9-bf17-bb7fc1d34a0b"

  val spiLoadedOperationId = Action.Id.fromString(SpiLoadedOperation.spiLoadedOperationUuid)

}

class SpiLoadedOperation extends Action0To1[A1] {

  override protected def execute()(context: ExecutionContext): A1 = ???

  @transient
  override lazy val tTagTO_0: ru.TypeTag[A1] = ru.typeTag[A1]

  override val id: Id = SpiLoadedOperation.spiLoadedOperationUuid

  override val name: String = "SpiLoadedOperation"

  override val description: String = "SpiLoadedOperation"

  override val specificParams: Array[Parameter[_]] = Array.empty

}

class TestSPIRegistration extends CatalogRegistrant {

  override def register(registrar: CatalogRegistrar): Unit = {
    val prios = SortPriority(12345).inSequence(10)
    registrar.registerOperation(ActionCategories.IO, () => new SpiLoadedOperation(), prios.next)
  }

}
