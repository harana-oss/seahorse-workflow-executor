package ai.deepsense.deeplang.actions.examples

import ai.deepsense.deeplang.actionobjects.MissingValuesHandler.MissingValueIndicatorChoice.No
import ai.deepsense.deeplang.actionobjects.MissingValuesHandler.Strategy.RemoveRow
import ai.deepsense.deeplang.actions.HandleMissingValues
import ai.deepsense.deeplang.parameters.selections.NameColumnSelection
import ai.deepsense.deeplang.parameters.selections.MultipleColumnSelection

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
