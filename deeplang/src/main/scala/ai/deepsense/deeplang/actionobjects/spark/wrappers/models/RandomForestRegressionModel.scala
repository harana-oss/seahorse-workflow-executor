package ai.deepsense.deeplang.actionobjects.spark.wrappers.models

import org.apache.spark.ml.regression.{RandomForestRegressionModel => SparkRFRModel}
import org.apache.spark.ml.regression.{RandomForestRegressor => SparkRFR}

import ai.deepsense.deeplang.actionobjects.report.CommonTablesGenerators.SparkSummaryEntry
import ai.deepsense.deeplang.actionobjects.report.CommonTablesGenerators
import ai.deepsense.deeplang.actionobjects.report.Report
import ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common.PredictorParams
import ai.deepsense.deeplang.actionobjects.LoadableWithFallback
import ai.deepsense.deeplang.actionobjects.SparkModelWrapper
import ai.deepsense.deeplang.parameters.Parameter
import ai.deepsense.sparkutils.ML

class RandomForestRegressionModel
    extends SparkModelWrapper[SparkRFRModel, SparkRFR]
    with LoadableWithFallback[SparkRFRModel, SparkRFR]
    with PredictorParams {

  override val params: Array[Parameter[_]] = Array(featuresColumn, predictionColumn)

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
