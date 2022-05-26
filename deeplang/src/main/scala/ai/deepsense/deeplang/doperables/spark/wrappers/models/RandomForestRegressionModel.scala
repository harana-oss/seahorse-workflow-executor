package ai.deepsense.deeplang.doperables.spark.wrappers.models

import org.apache.spark.ml.regression.{RandomForestRegressionModel => SparkRFRModel}
import org.apache.spark.ml.regression.{RandomForestRegressor => SparkRFR}

import ai.deepsense.deeplang.doperables.report.CommonTablesGenerators.SparkSummaryEntry
import ai.deepsense.deeplang.doperables.report.CommonTablesGenerators
import ai.deepsense.deeplang.doperables.report.Report
import ai.deepsense.deeplang.doperables.spark.wrappers.params.common.PredictorParams
import ai.deepsense.deeplang.doperables.LoadableWithFallback
import ai.deepsense.deeplang.doperables.SparkModelWrapper
import ai.deepsense.deeplang.params.Param
import ai.deepsense.sparkutils.ML

class RandomForestRegressionModel
    extends SparkModelWrapper[SparkRFRModel, SparkRFR]
    with LoadableWithFallback[SparkRFRModel, SparkRFR]
    with PredictorParams {

  override val params: Array[Param[_]] = Array(featuresColumn, predictionColumn)

  override def report(extended: Boolean = true): Report = {
    val summary =
      List(
        SparkSummaryEntry(
          name = "number of features",
          value = sparkModel.numFeatures,
          description = "Number of features the model was trained on."
        ),
        SparkSummaryEntry(
          name = "feature importances",
          value = sparkModel.featureImportances,
          description = "Estimate of the importance of each feature."
        )
      )

    val numTrees = ML.ModelParams.numTreesFromRandomForestRegressionModel(sparkModel)
    super
      .report(extended)
      .withReportName(s"${this.getClass.getSimpleName} with $numTrees trees")
      .withAdditionalTable(CommonTablesGenerators.modelSummary(summary))
      .withAdditionalTable(CommonTablesGenerators.decisionTree(sparkModel.treeWeights, sparkModel.trees), 2)
  }

  override def tryToLoadModel(path: String): Option[SparkRFRModel] = ML.ModelLoading.randomForestRegression(path)

}
