package ai.deepsense.deeplang.actions.exceptions

import org.apache.spark.sql.types.DataType

import ai.deepsense.commons.types.ColumnType.ColumnType

case class UnsupportedColumnTypeException(override val message: String)
    extends DOperationExecutionException(message, None)

object UnsupportedColumnTypeException {

  def apply(columnName: String, actualType: DataType): UnsupportedColumnTypeException =
    UnsupportedColumnTypeException(s"Column '$columnName' has unsupported type '${actualType.toString}'")

}
