package ai.deepsense.deeplang.doperations.examples

import ai.deepsense.deeplang.doperables.MissingValuesHandler.MissingValueIndicatorChoice.No
import ai.deepsense.deeplang.doperables.MissingValuesHandler.Strategy.RemoveRow
import ai.deepsense.deeplang.doperations.HandleMissingValues
import ai.deepsense.deeplang.params.selections.NameColumnSelection
import ai.deepsense.deeplang.params.selections.MultipleColumnSelection

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
