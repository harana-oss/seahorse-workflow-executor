package io.deepsense.deeplang.doperables.spark.wrappers.models

import org.apache.spark.ml.regression.{RandomForestRegressionModel => SparkRFRModel, RandomForestRegressor => SparkRFR}

import io.deepsense.deeplang.doperables.report.CommonTablesGenerators.SparkSummaryEntry
import io.deepsense.deeplang.doperables.report.{CommonTablesGenerators, Report}
import io.deepsense.deeplang.doperables.spark.wrappers.params.common.PredictorParams
import io.deepsense.deeplang.doperables.{LoadableWithFallback, SparkModelWrapper}
import io.deepsense.deeplang.params.Param
import io.deepsense.sparkutils.ML

class RandomForestRegressionModel
  extends SparkModelWrapper[SparkRFRModel, SparkRFR]
  with LoadableWithFallback[SparkRFRModel, SparkRFR]
  with PredictorParams {

  override val params: Array[Param[_]] = Array(
    featuresColumn,
    predictionColumn)

  override def report: Report = {
    val summary =
      List(
        SparkSummaryEntry(
          name = "number of features",
          value = sparkModel.numFeatures,
          description = "Number of features the model was trained on."),
        SparkSummaryEntry(
          name = "feature importances",
          value = sparkModel.featureImportances,
          description = "Estimate of the importance of each feature."
        ))

    val numTrees = ML.ModelParams.numTreesFromRandomForestRegressionModel(sparkModel)
    super.report
      .withReportName(
        s"${this.getClass.getSimpleName} with $numTrees trees")
      .withAdditionalTable(CommonTablesGenerators.modelSummary(summary))
      .withAdditionalTable(
        CommonTablesGenerators.decisionTree(
          sparkModel.treeWeights,
          sparkModel.trees),
        2)
  }

  override def tryToLoadModel(path: String): Option[SparkRFRModel] = ML.ModelLoading.randomForestRegression(path)
}
