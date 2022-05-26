package ai.deepsense.deeplang.doperables.spark.wrappers.models

import org.apache.spark.ml.regression.{GBTRegressionModel => SparkGBTRegressionModel}
import org.apache.spark.ml.regression.{GBTRegressor => SparkGBTRegressor}

import ai.deepsense.deeplang.doperables.report.CommonTablesGenerators.SparkSummaryEntry
import ai.deepsense.deeplang.doperables.report.CommonTablesGenerators
import ai.deepsense.deeplang.doperables.report.Report
import ai.deepsense.deeplang.doperables.spark.wrappers.params.common.PredictorParams
import ai.deepsense.deeplang.doperables.LoadableWithFallback
import ai.deepsense.deeplang.doperables.SparkModelWrapper
import ai.deepsense.deeplang.params.Param
import ai.deepsense.sparkutils.ML

class GBTRegressionModel
    extends SparkModelWrapper[SparkGBTRegressionModel, SparkGBTRegressor]
    with LoadableWithFallback[SparkGBTRegressionModel, SparkGBTRegressor]
    with PredictorParams {

  override val params: Array[Param[_]] = Array(featuresColumn, predictionColumn)

  override def report(extended: Boolean = true): Report = {
    val summary =
      List(
        SparkSummaryEntry(
          name = "number of features",
          value = sparkModel.numFeatures,
          description = "Number of features the model was trained on."
        )
      )

    super
      .report(extended)
      .withReportName(s"${this.getClass.getSimpleName} with ${sparkModel.numTrees} trees")
      .withAdditionalTable(CommonTablesGenerators.modelSummary(summary))
      .withAdditionalTable(CommonTablesGenerators.decisionTree(sparkModel.treeWeights, sparkModel.trees), 2)
  }

  override def tryToLoadModel(path: String): Option[SparkGBTRegressionModel] = ML.ModelLoading.GBTRegression(path)

}
