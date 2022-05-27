package ai.deepsense.deeplang.catalogs.actions.exceptions

import ai.deepsense.deeplang.Action

case class ActionNotFoundException(operationId: Action.Id)
    extends ActionsCatalogException(s"Action not found: $operationId")
