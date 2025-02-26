package ai.deepsense.deeplang.actions.exceptions

import ai.deepsense.commons.types.ColumnType
import ColumnType.ColumnType

case class WrongColumnTypeException(override val message: String) extends ActionExecutionException(message, None)

object WrongColumnTypeException {

  def apply(columnName: String, actualType: ColumnType, expectedTypes: ColumnType*): WrongColumnTypeException =
    WrongColumnTypeException(
      s"Column '$columnName' has type '$actualType' instead of " +
        s"expected ${expectedTypes.map(t => s"'${t.toString}'").mkString(" or ")}."
    )

}
