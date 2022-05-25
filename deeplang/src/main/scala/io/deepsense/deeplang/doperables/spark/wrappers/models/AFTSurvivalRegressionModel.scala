package io.deepsense.deeplang.doperables.spark.wrappers.models

import org.apache.spark.ml.regression.{AFTSurvivalRegression => SparkAFTSurvivalRegression, AFTSurvivalRegressionModel => SparkAFTSurvivalRegressionModel}

import io.deepsense.deeplang.ExecutionContext
import io.deepsense.deeplang.doperables.SparkModelWrapper
import io.deepsense.deeplang.doperables.report.CommonTablesGenerators.SparkSummaryEntry
import io.deepsense.deeplang.doperables.report.{CommonTablesGenerators, Report}
import io.deepsense.deeplang.doperables.serialization.SerializableSparkModel
import io.deepsense.deeplang.doperables.spark.wrappers.params.AFTSurvivalRegressionParams
import io.deepsense.deeplang.doperables.spark.wrappers.params.common.PredictorParams
import io.deepsense.deeplang.params.Param


class AFTSurvivalRegressionModel
  extends SparkModelWrapper[
    SparkAFTSurvivalRegressionModel,
    SparkAFTSurvivalRegression]
  with PredictorParams
  with AFTSurvivalRegressionParams {

  override val params: Array[Param[_]] = Array(
    featuresColumn,
    predictionColumn,
    quantileProbabilities,
    optionalQuantilesColumn)

  override def report: Report = {
    val summary =
      List(
        SparkSummaryEntry(
          name = "coefficients",
          value = sparkModel.coefficients,
          description = "Regression coefficients vector of the beta parameter."),
        SparkSummaryEntry(
          name = "intercept",
          value = sparkModel.intercept,
          description = "Intercept of the beta parameter."),
        SparkSummaryEntry(
          name = "scale",
          value = sparkModel.scale,
          description = "The log of scale parameter - log(sigma).")
      )

    super.report
      .withAdditionalTable(CommonTablesGenerators.modelSummary(summary))
  }

  override protected def loadModel(
      ctx: ExecutionContext,
      path: String): SerializableSparkModel[SparkAFTSurvivalRegressionModel] = {
    new SerializableSparkModel(SparkAFTSurvivalRegressionModel.load(path))
  }
}
