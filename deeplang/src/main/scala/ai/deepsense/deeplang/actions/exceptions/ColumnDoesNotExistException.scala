package ai.deepsense.deeplang.actions.exceptions

import org.apache.spark.sql.types.StructType

import ai.deepsense.deeplang.actions.exceptions.ColumnDoesNotExistException._
import ai.deepsense.deeplang.parameters.selections.IndexSingleColumnSelection
import ai.deepsense.deeplang.parameters.selections.NameSingleColumnSelection
import ai.deepsense.deeplang.parameters.selections.SingleColumnSelection

case class ColumnDoesNotExistException(selection: SingleColumnSelection, schema: StructType)
    extends ActionExecutionException(exceptionMessage(selection, schema), None)

object ColumnDoesNotExistException {

  private def exceptionMessage(selection: SingleColumnSelection, schema: StructType): String =
    s"Column ${selectionDescription(selection)} " +
      s"does not exist in the input DataFrame (${schemaDescription(selection, schema)})"

  private def selectionDescription(selection: SingleColumnSelection): String =
    selection match {
      case NameSingleColumnSelection(name)   => s"`$name`"
      case IndexSingleColumnSelection(index) => s"with index $index"
    }

  private def schemaDescription(selection: SingleColumnSelection, schema: StructType): String = {
    selection match {
      case IndexSingleColumnSelection(_) =>
        s"index range: 0..${schema.length - 1}"
      case NameSingleColumnSelection(_)  =>
        s"column names: ${schema.fields.map(field => s"`${field.name}`").mkString(", ")}"
    }
  }

}
