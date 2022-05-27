package ai.deepsense.deeplang.inference.exceptions

import ai.deepsense.deeplang.exceptions.FlowException
import ai.deepsense.deeplang.parameters.selections.MultipleColumnSelection

case class SelectedIncorrectColumnsNumber(
    multipleColumnSelection: MultipleColumnSelection,
    selectedColumnsNames: Seq[String],
    modelsCount: Int
) extends FlowException(
      s"The selection '$multipleColumnSelection' selects " +
        s"${selectedColumnsNames.size} column(s): ${selectedColumnsNames.mkString(", ")}. " +
        s"Expected to select $modelsCount column(s)."
    )
