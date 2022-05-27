package ai.deepsense.deeplang.catalogs.actions.exceptions

import scala.reflect.runtime.universe.Type

case class NoParameterlessConstructorInActionException(operationType: Type)
    extends ActionsCatalogException(
      "Registered Action has to have parameterless constructor" +
        s"(Action ${operationType.typeSymbol.name} has no parameterless constructor)"
    )
