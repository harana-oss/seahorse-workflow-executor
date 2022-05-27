package ai.deepsense.deeplang.actions.examples

import ai.deepsense.deeplang.actionobjects.multicolumn.MultiColumnParams.SingleOrMultiColumnChoices.SingleColumnChoice
import ai.deepsense.deeplang.actionobjects.multicolumn.SingleColumnParams.SingleTransformInPlaceChoices.NoInPlaceChoice
import ai.deepsense.deeplang.actions.SqlColumnTransformation
import ai.deepsense.deeplang.parameters.selections.NameSingleColumnSelection

class SqlColumnTransformationExample extends AbstractOperationExample[SqlColumnTransformation] {

  override def dOperation: SqlColumnTransformation = {
    val o               = new SqlColumnTransformation()
    val myalias: String = "myAlias"

    val inPlace = NoInPlaceChoice()
      .setOutputColumn("WeightCutoff")
    val single  = SingleColumnChoice()
      .setInputColumn(NameSingleColumnSelection("Weight"))
      .setInPlace(inPlace)
    o.transformer
      .setFormula("MINIMUM(" + myalias + ", 2.0)")
      .setInputColumnAlias(myalias)
      .setSingleOrMultiChoice(single)
    o.set(o.transformer.extractParamMap())
  }

  override def fileNames: Seq[String] = Seq("example_animals")

}
