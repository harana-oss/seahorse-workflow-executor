package ai.deepsense.deeplang.doperables.spark.wrappers.models

import org.apache.spark.ml.regression.LinearRegressionTrainingSummary
import org.apache.spark.ml.regression.{LinearRegression => SparkLinearRegression}
import org.apache.spark.ml.regression.{LinearRegressionModel => SparkLinearRegressionModel}

import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.doperables.SparkModelWrapper
import ai.deepsense.deeplang.doperables.report.CommonTablesGenerators.SparkSummaryEntry
import ai.deepsense.deeplang.doperables.report.CommonTablesGenerators
import ai.deepsense.deeplang.doperables.report.Report
import ai.deepsense.deeplang.doperables.serialization.SerializableSparkModel
import ai.deepsense.deeplang.doperables.spark.wrappers.params.common.PredictorParams
import ai.deepsense.deeplang.params.Param

class LinearRegressionModel
    extends SparkModelWrapper[SparkLinearRegressionModel, SparkLinearRegression]
    with PredictorParams {

  override val params: Array[Param[_]] = Array(featuresColumn, predictionColumn)

  override def report(extended: Boolean = true): Report = {
    val coefficients =
      SparkSummaryEntry(
        name = "coefficients",
        value = sparkModel.coefficients,
        description = "Weights computed for every feature."
      )

    val summary = if (sparkModel.hasSummary) {
      val modelSummary: LinearRegressionTrainingSummary = sparkModel.summary
      List(
        SparkSummaryEntry(
          name = "explained variance",
          value = modelSummary.explainedVariance,
          description = "Explained variance regression score."
        ),
        SparkSummaryEntry(
          name = "mean absolute error",
          value = modelSummary.meanAbsoluteError,
          description = "Mean absolute error is a risk function corresponding to the " +
            "expected value of the absolute error loss or l1-norm loss."
        ),
        SparkSummaryEntry(
          name = "mean squared error",
          value = modelSummary.meanSquaredError,
          description = "Mean squared error is a risk function corresponding to the " +
            "expected value of the squared error loss or quadratic loss."
        ),
        SparkSummaryEntry(
          name = "root mean squared error",
          value = modelSummary.rootMeanSquaredError,
          description = "Root mean squared error is defined as the square root " +
            "of the mean squared error."
        ),
        SparkSummaryEntry(
          name = "R^2^",
          value = modelSummary.r2,
          description = "R^2^ is the coefficient of determination."
        ),
        SparkSummaryEntry(
          name = "objective history",
          value = modelSummary.objectiveHistory,
          description = "Objective function (scaled loss + regularization) at each iteration."
        ),
        SparkSummaryEntry(
          name = "total iterations",
          value = modelSummary.totalIterations,
          description = "Number of training iterations until termination."
        ),
        SparkSummaryEntry(
          name = "number of instances",
          value = modelSummary.numInstances,
          description = "Number of instances in DataFrame predictions."
        ),
        SparkSummaryEntry(
          name = "deviance residuals",
          value = modelSummary.devianceResiduals,
          description = "The weighted residuals, the usual residuals " +
            "rescaled by the square root of the instance weights."
        ),
        SparkSummaryEntry(
          name = "coefficient standard errors",
          value = modelSummary.coefficientStandardErrors,
          description = "Standard error of estimated coefficients and intercept."
        ),
        SparkSummaryEntry(
          name = "t-values",
          value = modelSummary.tValues,
          description = "T-statistic of estimated coefficients and intercept."
        ),
        SparkSummaryEntry(
          name = "p-values",
          value = modelSummary.pValues,
          description = "Two-sided p-value of estimated coefficients and intercept."
        )
      )
    } else
      Nil

    super
      .report(extended)
      .withAdditionalTable(CommonTablesGenerators.modelSummary(List(coefficients) ++ summary))
  }

  override protected def loadModel(
      ctx: ExecutionContext,
      path: String
  ): SerializableSparkModel[SparkLinearRegressionModel] =
    new SerializableSparkModel(SparkLinearRegressionModel.load(path))

}
