package io.deepsense.deeplang.doperations.exceptions

import io.deepsense.commons.types.ColumnType.ColumnType

case class MultipleTypesReplacementException(columnTypes: Map[String, ColumnType])
    extends DOperationExecutionException(
      "Missing value replacement is impossible - selected columns: " +
        s"${columnTypes.keys.mkString(", ")} have different column types: " +
        s"${columnTypes.keys.map(columnTypes(_)).mkString(", ")}",
      None
    )
