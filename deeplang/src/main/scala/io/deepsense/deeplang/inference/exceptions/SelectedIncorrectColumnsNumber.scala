package io.deepsense.deeplang.inference.exceptions

import io.deepsense.deeplang.exceptions.DeepLangException
import io.deepsense.deeplang.params.selections.MultipleColumnSelection

case class SelectedIncorrectColumnsNumber(
    multipleColumnSelection: MultipleColumnSelection,
    selectedColumnsNames: Seq[String],
    modelsCount: Int)
  extends DeepLangException(
    s"The selection '$multipleColumnSelection' selects " +
      s"${selectedColumnsNames.size} column(s): ${selectedColumnsNames.mkString(", ")}. " +
      s"Expected to select $modelsCount column(s).")
