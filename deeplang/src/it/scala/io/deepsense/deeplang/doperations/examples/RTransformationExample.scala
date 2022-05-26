package io.deepsense.deeplang.doperations.examples

import io.deepsense.deeplang.doperables.RTransformer
import io.deepsense.deeplang.doperables.dataframe.DataFrame
import io.deepsense.deeplang.doperations.RTransformation
import io.deepsense.deeplang.DOperable
import io.deepsense.deeplang.ExecutionContext

class RTransformationExample extends AbstractOperationExample[RTransformation] {

  // This is mocked because R executor is not available in tests.
  class RTransformationMock extends RTransformation {

    override def execute(arg: DataFrame)(context: ExecutionContext): (DataFrame, RTransformer) =
      (PythonTransformationExample.execute(arg)(context), mock[RTransformer])

  }

  override def dOperation: RTransformation = {
    val op = new RTransformationMock()
    op.transformer
      .setCodeParameter(
        "transform <- function(dataframe) {" +
          "\n  filtered_df <- filter(dataframe, dataframe$temp > 0.4)" +
          "\n  sorted_filtered_df <- orderBy(filtered_df, desc(filtered_df$windspeed))" +
          "\n  return(sorted_filtered_df)" +
          "\n}"
      )
    op.set(op.transformer.extractParamMap())

  }

  override def fileNames: Seq[String] = Seq("example_datetime_windspeed_hum_temp")

}
