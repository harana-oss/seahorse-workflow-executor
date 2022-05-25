package io.deepsense.deeplang.doperations.examples

import io.deepsense.deeplang.doperables.multicolumn.MultiColumnParams.SingleOrMultiColumnChoices.SingleColumnChoice
import io.deepsense.deeplang.doperables.multicolumn.SingleColumnParams.SingleTransformInPlaceChoices.NoInPlaceChoice
import io.deepsense.deeplang.doperations.SqlColumnTransformation
import io.deepsense.deeplang.params.selections.NameSingleColumnSelection

class SqlColumnTransformationExample extends AbstractOperationExample[SqlColumnTransformation] {

  override def dOperation: SqlColumnTransformation = {
    val o = new SqlColumnTransformation()
    val myalias: String = "myAlias"

    val inPlace = NoInPlaceChoice()
      .setOutputColumn("WeightCutoff")
    val single = SingleColumnChoice()
      .setInputColumn(NameSingleColumnSelection("Weight"))
      .setInPlace(inPlace)
    o.transformer.setFormula("MINIMUM(" + myalias + ", 2.0)")
      .setInputColumnAlias(myalias)
      .setSingleOrMultiChoice(single)
    o.set(o.transformer.extractParamMap())
  }

  override def fileNames: Seq[String] = Seq("example_animals")
}
