package ai.deepsense.deeplang.doperables.spark.wrappers.models

import org.apache.spark.ml.regression.{IsotonicRegression => SparkIsotonicRegression}
import org.apache.spark.ml.regression.{IsotonicRegressionModel => SparkIsotonicRegressionModel}

import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.doperables.SparkModelWrapper
import ai.deepsense.deeplang.doperables.report.CommonTablesGenerators.SparkSummaryEntry
import ai.deepsense.deeplang.doperables.report.CommonTablesGenerators
import ai.deepsense.deeplang.doperables.report.Report
import ai.deepsense.deeplang.doperables.serialization.SerializableSparkModel
import ai.deepsense.deeplang.doperables.spark.wrappers.params.common.HasFeatureIndexParam
import ai.deepsense.deeplang.doperables.spark.wrappers.params.common.PredictorParams

class IsotonicRegressionModel
    extends SparkModelWrapper[SparkIsotonicRegressionModel, SparkIsotonicRegression]
    with PredictorParams
    with HasFeatureIndexParam {

  override val params: Array[ai.deepsense.deeplang.params.Param[_]] =
    Array(featureIndex, featuresColumn, predictionColumn)

  override def report(extended: Boolean = true): Report = {
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
    super
      .report(extended)
      .withAdditionalTable(CommonTablesGenerators.modelSummary(summary))
  }

  override protected def loadModel(
      ctx: ExecutionContext,
      path: String
  ): SerializableSparkModel[SparkIsotonicRegressionModel] =
    new SerializableSparkModel(SparkIsotonicRegressionModel.load(path))

}
