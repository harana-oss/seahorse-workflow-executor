package ai.deepsense.deeplang.catalogs.actions.exceptions

import ai.deepsense.deeplang.Action

case class DOperationNotFoundException(operationId: Action.Id)
    extends ActionsCatalogException(s"DOperation not found: $operationId")
