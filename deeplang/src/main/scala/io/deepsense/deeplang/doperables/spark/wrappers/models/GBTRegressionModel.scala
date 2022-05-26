package io.deepsense.deeplang.doperables.spark.wrappers.models

import org.apache.spark.ml.regression.{GBTRegressionModel => SparkGBTRegressionModel}
import org.apache.spark.ml.regression.{GBTRegressor => SparkGBTRegressor}

import io.deepsense.deeplang.doperables.report.CommonTablesGenerators.SparkSummaryEntry
import io.deepsense.deeplang.doperables.report.CommonTablesGenerators
import io.deepsense.deeplang.doperables.report.Report
import io.deepsense.deeplang.doperables.spark.wrappers.params.common.PredictorParams
import io.deepsense.deeplang.doperables.LoadableWithFallback
import io.deepsense.deeplang.doperables.SparkModelWrapper
import io.deepsense.deeplang.params.Param
import io.deepsense.sparkutils.ML

class GBTRegressionModel
    extends SparkModelWrapper[SparkGBTRegressionModel, SparkGBTRegressor]
    with LoadableWithFallback[SparkGBTRegressionModel, SparkGBTRegressor]
    with PredictorParams {

  override val params: Array[Param[_]] = Array(featuresColumn, predictionColumn)

  override def report: Report = {
    val summary =
      List(
        SparkSummaryEntry(
          name = "number of features",
          value = sparkModel.numFeatures,
          description = "Number of features the model was trained on."
        )
      )

    super.report
      .withReportName(s"${this.getClass.getSimpleName} with ${sparkModel.getNumTrees} trees")
      .withAdditionalTable(CommonTablesGenerators.modelSummary(summary))
      .withAdditionalTable(CommonTablesGenerators.decisionTree(sparkModel.treeWeights, sparkModel.trees), 2)
  }

  override def tryToLoadModel(path: String): Option[SparkGBTRegressionModel] = ML.ModelLoading.GBTRegression(path)

}
