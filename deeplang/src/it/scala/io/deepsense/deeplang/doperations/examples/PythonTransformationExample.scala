package io.deepsense.deeplang.doperations.examples

import org.apache.spark.sql.functions._

import io.deepsense.deeplang.doperables.PythonTransformer
import io.deepsense.deeplang.doperables.dataframe.DataFrame
import io.deepsense.deeplang.{DOperable, ExecutionContext}
import io.deepsense.deeplang.doperations.PythonTransformation

class PythonTransformationExample extends AbstractOperationExample[PythonTransformation] {

  // This is mocked because Python executor is not available in tests.
  class PythonTransformationMock extends PythonTransformation {
    override def execute(arg: DataFrame)(context: ExecutionContext): (DataFrame, PythonTransformer) = {
      (PythonTransformationExample.execute(arg)(context), mock[PythonTransformer])
    }
  }

  override def dOperation: PythonTransformation = {
    val op = new PythonTransformationMock()
    op.transformer
      .setCodeParameter(
        "def transform(df):" +
          "\n    return df.filter(df.temp > 0.4).sort(df.windspeed, ascending=False)")
    op.set(op.transformer.extractParamMap())

  }

  override def fileNames: Seq[String] = Seq("example_datetime_windspeed_hum_temp")
}

object PythonTransformationExample {
  def execute(arg: DataFrame)(context: ExecutionContext): DataFrame = {
    val resultSparkDataFrame = arg.sparkDataFrame.filter("temp > 0.4").sort(desc("windspeed"))
    DataFrame.fromSparkDataFrame(resultSparkDataFrame)
  }
}
