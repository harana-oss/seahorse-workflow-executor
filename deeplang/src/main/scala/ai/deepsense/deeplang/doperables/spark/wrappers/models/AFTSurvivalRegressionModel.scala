package ai.deepsense.deeplang.doperables.spark.wrappers.models

import org.apache.spark.ml.regression.{AFTSurvivalRegression => SparkAFTSurvivalRegression}
import org.apache.spark.ml.regression.{AFTSurvivalRegressionModel => SparkAFTSurvivalRegressionModel}

import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.doperables.SparkModelWrapper
import ai.deepsense.deeplang.doperables.report.CommonTablesGenerators.SparkSummaryEntry
import ai.deepsense.deeplang.doperables.report.CommonTablesGenerators
import ai.deepsense.deeplang.doperables.report.Report
import ai.deepsense.deeplang.doperables.serialization.SerializableSparkModel
import ai.deepsense.deeplang.doperables.spark.wrappers.params.AFTSurvivalRegressionParams
import ai.deepsense.deeplang.doperables.spark.wrappers.params.common.PredictorParams
import ai.deepsense.deeplang.params.Param

class AFTSurvivalRegressionModel
    extends SparkModelWrapper[SparkAFTSurvivalRegressionModel, SparkAFTSurvivalRegression]
    with PredictorParams
    with AFTSurvivalRegressionParams {

  override val params: Array[Param[_]] =
    Array(featuresColumn, predictionColumn, quantileProbabilities, optionalQuantilesColumn)

  override def report(extended: Boolean = true): Report = {
    val summary =
      List(
        SparkSummaryEntry(
          name = "coefficients",
          value = sparkModel.coefficients,
          description = "Regression coefficients vector of the beta parameter."
        ),
        SparkSummaryEntry(
          name = "intercept",
          value = sparkModel.intercept,
          description = "Intercept of the beta parameter."
        ),
        SparkSummaryEntry(
          name = "scale",
          value = sparkModel.scale,
          description = "The log of scale parameter - log(sigma)."
        )
      )

    super
      .report(extended)
      .withAdditionalTable(CommonTablesGenerators.modelSummary(summary))
  }

  override protected def loadModel(
      ctx: ExecutionContext,
      path: String
  ): SerializableSparkModel[SparkAFTSurvivalRegressionModel] =
    new SerializableSparkModel(SparkAFTSurvivalRegressionModel.load(path))

}
