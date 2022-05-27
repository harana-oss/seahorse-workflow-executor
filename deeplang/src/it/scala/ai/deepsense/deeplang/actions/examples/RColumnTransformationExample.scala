package ai.deepsense.deeplang.actions.examples

import ai.deepsense.deeplang.actionobjects.RColumnTransformer
import ai.deepsense.deeplang.actionobjects.TargetTypeChoices
import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.actionobjects.multicolumn.MultiColumnParams.SingleOrMultiColumnChoices.SingleColumnChoice
import ai.deepsense.deeplang.actionobjects.multicolumn.SingleColumnParams.SingleTransformInPlaceChoices.NoInPlaceChoice
import ai.deepsense.deeplang.actions.RColumnTransformation
import ai.deepsense.deeplang.parameters.selections.NameSingleColumnSelection
import ai.deepsense.deeplang.ActionObject
import ai.deepsense.deeplang.ExecutionContext

class RColumnTransformationExample extends AbstractOperationExample[RColumnTransformation] {

  val poundInKg = 0.45359237

  val inputColumnName = "Weight"

  val outputColumnName = "WeightInPounds"

  // This is mocked because R executor is not available in tests.
  class RColumnTransformationMock extends RColumnTransformation {

    override def execute(arg: DataFrame)(context: ExecutionContext): (DataFrame, RColumnTransformer) = {
      val sdf                  = arg.sparkDataFrame
      val resultSparkDataFrame = sdf.select(sdf("*"), (sdf(inputColumnName) / poundInKg).alias(outputColumnName))
      (DataFrame.fromSparkDataFrame(resultSparkDataFrame), mock[RColumnTransformer])
    }

  }

  override def dOperation: RColumnTransformation = {
    val o = new RColumnTransformationMock()

    val inPlace = NoInPlaceChoice()
      .setOutputColumn(s"$outputColumnName")
    val single  = SingleColumnChoice()
      .setInputColumn(NameSingleColumnSelection(inputColumnName))
      .setInPlace(inPlace)
    o.transformer
      .setTargetType(TargetTypeChoices.DoubleTargetTypeChoice())
      .setSingleOrMultiChoice(single)
      .setCodeParameter(
        "transform.column <- function(column, column.name) {" +
          s"\n  return(column / $poundInKg)" +
          "\n}"
      )
    o.set(o.transformer.extractParamMap())
  }

  override def fileNames: Seq[String] = Seq("example_animals")

}
