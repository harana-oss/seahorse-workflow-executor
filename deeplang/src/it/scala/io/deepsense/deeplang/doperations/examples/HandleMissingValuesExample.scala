package io.deepsense.deeplang.doperations.examples

import io.deepsense.deeplang.doperables.MissingValuesHandler.MissingValueIndicatorChoice.No
import io.deepsense.deeplang.doperables.MissingValuesHandler.Strategy.RemoveRow
import io.deepsense.deeplang.doperations.HandleMissingValues
import io.deepsense.deeplang.params.selections.NameColumnSelection
import io.deepsense.deeplang.params.selections.MultipleColumnSelection

class HandleMissingValuesExample extends AbstractOperationExample[HandleMissingValues] {

  override def dOperation: HandleMissingValues = {
    val op = new HandleMissingValues()
    op.transformer
      .setUserDefinedMissingValues(Seq("-1.0"))
      .setSelectedColumns(MultipleColumnSelection(Vector(NameColumnSelection(Set("baths", "price")))))
      .setStrategy(RemoveRow())
      .setMissingValueIndicator(No())
    op.set(op.transformer.extractParamMap())
  }

  override def fileNames: Seq[String] = Seq("example_missing_values")

}
