package ai.deepsense.deeplang.actions.examples

import org.apache.spark.sql.functions.when

import ai.deepsense.deeplang.ActionObject
import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.actionobjects.PythonColumnTransformer
import ai.deepsense.deeplang.actionobjects.TargetTypeChoices
import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.actionobjects.multicolumn.MultiColumnParams.SingleOrMultiColumnChoices.SingleColumnChoice
import ai.deepsense.deeplang.actionobjects.multicolumn.SingleColumnParams.SingleTransformInPlaceChoices.NoInPlaceChoice
import ai.deepsense.deeplang.actions.PythonColumnTransformation
import ai.deepsense.deeplang.parameters.selections.NameSingleColumnSelection

class PythonColumnTransformationExample extends AbstractOperationExample[PythonColumnTransformation] {

  val inputColumnName = "Weight"

  val outputColumnName = "WeightCutoff"

  // This is mocked because Python executor is not available in tests.
  class PythonColumnTransformationMock extends PythonColumnTransformation {

    override def execute(arg: DataFrame)(context: ExecutionContext): (DataFrame, PythonColumnTransformer) = {
      val sdf                  = arg.sparkDataFrame
      val resultSparkDataFrame = sdf.select(
        sdf("*"),
        when(sdf(inputColumnName) > 2.0, 2.0)
          .otherwise(sdf(inputColumnName))
          .alias(outputColumnName)
      )
      (DataFrame.fromSparkDataFrame(resultSparkDataFrame), mock[PythonColumnTransformer])
    }

  }

  override def dOperation: PythonColumnTransformation = {
    val op = new PythonColumnTransformationMock()

    val inPlace = NoInPlaceChoice()
      .setOutputColumn(s"$outputColumnName")
    val single  = SingleColumnChoice()
      .setInputColumn(NameSingleColumnSelection(inputColumnName))
      .setInPlace(inPlace)
    op.transformer
      .setTargetType(TargetTypeChoices.DoubleTargetTypeChoice())
      .setSingleOrMultiChoice(single)
      .setCodeParameter(
        "def transform_value(value, column_name):\n" +
          "    return min(value, 2.0)"
      )
    op.set(op.transformer.extractParamMap())
  }

  override def fileNames: Seq[String] = Seq("example_animals")

}
