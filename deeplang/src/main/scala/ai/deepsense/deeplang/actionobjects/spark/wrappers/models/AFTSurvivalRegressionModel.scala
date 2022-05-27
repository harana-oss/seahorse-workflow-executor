package ai.deepsense.deeplang.actionobjects.spark.wrappers.models

import org.apache.spark.ml.regression.{AFTSurvivalRegression => SparkAFTSurvivalRegression}
import org.apache.spark.ml.regression.{AFTSurvivalRegressionModel => SparkAFTSurvivalRegressionModel}

import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.actionobjects.SparkModelWrapper
import ai.deepsense.deeplang.actionobjects.report.CommonTablesGenerators.SparkSummaryEntry
import ai.deepsense.deeplang.actionobjects.report.CommonTablesGenerators
import ai.deepsense.deeplang.actionobjects.report.Report
import ai.deepsense.deeplang.actionobjects.serialization.SerializableSparkModel
import ai.deepsense.deeplang.actionobjects.spark.wrappers.params.AFTSurvivalRegressionParams
import ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common.PredictorParams
import ai.deepsense.deeplang.parameters.Parameter

class AFTSurvivalRegressionModel
    extends SparkModelWrapper[SparkAFTSurvivalRegressionModel, SparkAFTSurvivalRegression]
    with PredictorParams
    with AFTSurvivalRegressionParams {

  override val params: Array[Parameter[_]] =
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
