package ai.deepsense.deeplang.catalogs.doperations.exceptions

import ai.deepsense.deeplang.DOperation

case class DOperationNotFoundException(operationId: DOperation.Id)
    extends DOperationsCatalogException(s"DOperation not found: $operationId")
