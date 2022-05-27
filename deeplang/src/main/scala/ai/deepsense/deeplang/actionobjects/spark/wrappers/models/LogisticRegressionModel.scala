package ai.deepsense.deeplang.actionobjects.spark.wrappers.models

import org.apache.spark.ml.classification.LogisticRegressionTrainingSummary
import org.apache.spark.ml.classification.{LogisticRegression => SparkLogisticRegression}
import org.apache.spark.ml.classification.{LogisticRegressionModel => SparkLogisticRegressionModel}

import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.actionobjects.SparkModelWrapper
import ai.deepsense.deeplang.actionobjects.report.CommonTablesGenerators.SparkSummaryEntry
import ai.deepsense.deeplang.actionobjects.report.CommonTablesGenerators
import ai.deepsense.deeplang.actionobjects.report.Report
import ai.deepsense.deeplang.actionobjects.serialization.SerializableSparkModel
import ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common.HasThreshold
import ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common.ProbabilisticClassifierParams
import ai.deepsense.deeplang.parameters.Parameter

class LogisticRegressionModel
    extends SparkModelWrapper[SparkLogisticRegressionModel, SparkLogisticRegression]
    with ProbabilisticClassifierParams
    with HasThreshold {

  override val params: Array[Parameter[_]] =
    Array(featuresColumn, probabilityColumn, rawPredictionColumn, predictionColumn, threshold)

  override def report(extended: Boolean = true): Report = {
    val coefficients =
      SparkSummaryEntry(
        name = "coefficients",
        value = sparkModel.coefficients,
        description = "Weights computed for every feature."
      )

    val summary = if (sparkModel.hasSummary) {
      val modelSummary: LogisticRegressionTrainingSummary = sparkModel.summary
      List(
        SparkSummaryEntry(
          name = "objective history",
          value = modelSummary.objectiveHistory,
          description = "Objective function (scaled loss + regularization) at each iteration."
        ),
        SparkSummaryEntry(
          name = "total iterations",
          value = modelSummary.totalIterations,
          description = "Number of training iterations until termination."
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
  ): SerializableSparkModel[SparkLogisticRegressionModel] =
    new SerializableSparkModel(SparkLogisticRegressionModel.load(path))

}
