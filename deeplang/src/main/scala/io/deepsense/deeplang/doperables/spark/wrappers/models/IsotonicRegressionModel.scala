package io.deepsense.deeplang.doperables.spark.wrappers.models

import org.apache.spark.ml.regression.{IsotonicRegression => SparkIsotonicRegression}
import org.apache.spark.ml.regression.{IsotonicRegressionModel => SparkIsotonicRegressionModel}

import io.deepsense.deeplang.ExecutionContext
import io.deepsense.deeplang.doperables.SparkModelWrapper
import io.deepsense.deeplang.doperables.report.CommonTablesGenerators.SparkSummaryEntry
import io.deepsense.deeplang.doperables.report.CommonTablesGenerators
import io.deepsense.deeplang.doperables.report.Report
import io.deepsense.deeplang.doperables.serialization.SerializableSparkModel
import io.deepsense.deeplang.doperables.spark.wrappers.params.common.HasFeatureIndexParam
import io.deepsense.deeplang.doperables.spark.wrappers.params.common.PredictorParams

class IsotonicRegressionModel
    extends SparkModelWrapper[SparkIsotonicRegressionModel, SparkIsotonicRegression]
    with PredictorParams
    with HasFeatureIndexParam {

  override val params: Array[io.deepsense.deeplang.params.Param[_]] =
    Array(featureIndex, featuresColumn, predictionColumn)

  override def report: Report = {
    val summary =
      List(
        SparkSummaryEntry(
          name = "boundaries",
          value = sparkModel.boundaries,
          description = "Boundaries in increasing order for which predictions are known."
        ),
        SparkSummaryEntry(
          name = "predictions",
          value = sparkModel.predictions,
          description = "Predictions associated with the boundaries at the same index, " +
            "monotone because of isotonic regression."
        )
      )
    super.report
      .withAdditionalTable(CommonTablesGenerators.modelSummary(summary))
  }

  override protected def loadModel(
      ctx: ExecutionContext,
      path: String
  ): SerializableSparkModel[SparkIsotonicRegressionModel] =
    new SerializableSparkModel(SparkIsotonicRegressionModel.load(path))

}
