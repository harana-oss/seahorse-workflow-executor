package ai.deepsense.deeplang.catalogs.actions.exceptions

import scala.reflect.runtime.universe.Type

case class NoParameterlessConstructorInDOperationException(operationType: Type)
    extends ActionsCatalogException(
      "Registered DOperation has to have parameterless constructor" +
        s"(DOperation ${operationType.typeSymbol.name} has no parameterless constructor)"
    )
