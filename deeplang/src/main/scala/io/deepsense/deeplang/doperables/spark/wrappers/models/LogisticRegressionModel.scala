package io.deepsense.deeplang.doperables.spark.wrappers.models

import org.apache.spark.ml.classification.{LogisticRegressionTrainingSummary, LogisticRegression => SparkLogisticRegression, LogisticRegressionModel => SparkLogisticRegressionModel}

import io.deepsense.deeplang.ExecutionContext
import io.deepsense.deeplang.doperables.SparkModelWrapper
import io.deepsense.deeplang.doperables.report.CommonTablesGenerators.SparkSummaryEntry
import io.deepsense.deeplang.doperables.report.{CommonTablesGenerators, Report}
import io.deepsense.deeplang.doperables.serialization.SerializableSparkModel
import io.deepsense.deeplang.doperables.spark.wrappers.params.common.{HasThreshold, ProbabilisticClassifierParams}
import io.deepsense.deeplang.params.Param

class LogisticRegressionModel
  extends SparkModelWrapper[
    SparkLogisticRegressionModel,
    SparkLogisticRegression]
  with ProbabilisticClassifierParams
  with HasThreshold {

  override val params: Array[Param[_]] = Array(
    featuresColumn,
    probabilityColumn,
    rawPredictionColumn,
    predictionColumn,
    threshold)

  override def report: Report = {
    val coefficients =
      SparkSummaryEntry(
        name = "coefficients",
        value = sparkModel.coefficients,
        description = "Weights computed for every feature.")

    val summary = if (sparkModel.hasSummary) {
      val modelSummary: LogisticRegressionTrainingSummary = sparkModel.summary
      List(
        SparkSummaryEntry(
          name = "objective history",
          value = modelSummary.objectiveHistory,
          description = "Objective function (scaled loss + regularization) at each iteration."),
        SparkSummaryEntry(
          name = "total iterations",
          value = modelSummary.totalIterations,
          description = "Number of training iterations until termination."))
    } else {
      Nil
    }

    super.report
      .withAdditionalTable(CommonTablesGenerators.modelSummary(List(coefficients) ++ summary))
  }

  override protected def loadModel(
      ctx: ExecutionContext,
      path: String): SerializableSparkModel[SparkLogisticRegressionModel] = {
    new SerializableSparkModel(SparkLogisticRegressionModel.load(path))
  }
}
