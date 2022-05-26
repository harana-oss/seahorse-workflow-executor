package io.deepsense.deeplang.doperations.examples

import org.apache.spark.sql.functions.when

import io.deepsense.deeplang.DOperable
import io.deepsense.deeplang.ExecutionContext
import io.deepsense.deeplang.doperables.PythonColumnTransformer
import io.deepsense.deeplang.doperables.TargetTypeChoices
import io.deepsense.deeplang.doperables.dataframe.DataFrame
import io.deepsense.deeplang.doperables.multicolumn.MultiColumnParams.SingleOrMultiColumnChoices.SingleColumnChoice
import io.deepsense.deeplang.doperables.multicolumn.SingleColumnParams.SingleTransformInPlaceChoices.NoInPlaceChoice
import io.deepsense.deeplang.doperations.PythonColumnTransformation
import io.deepsense.deeplang.params.selections.NameSingleColumnSelection

class PythonColumnTransformationExample extends AbstractOperationExample[PythonColumnTransformation] {

  val inputColumnName = "Weight"

  val outputColumnName = "WeightCutoff"

  // This is mocked because Python executor is not available in tests.
  class PythonColumnTransformationMock extends PythonColumnTransformation {

    override def execute(arg: DataFrame)(context: ExecutionContext): (DataFrame, PythonColumnTransformer) = {
      val sdf = arg.sparkDataFrame
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
    val single = SingleColumnChoice()
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
