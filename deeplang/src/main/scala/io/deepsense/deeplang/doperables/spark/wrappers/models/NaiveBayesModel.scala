package io.deepsense.deeplang.doperables.spark.wrappers.models

import org.apache.spark.ml.classification.{NaiveBayes => SparkNaiveBayes}
import org.apache.spark.ml.classification.{NaiveBayesModel => SparkNaiveBayesModel}

import io.deepsense.deeplang.ExecutionContext
import io.deepsense.deeplang.doperables.SparkModelWrapper
import io.deepsense.deeplang.doperables.report.CommonTablesGenerators.SparkSummaryEntry
import io.deepsense.deeplang.doperables.report.CommonTablesGenerators
import io.deepsense.deeplang.doperables.report.Report
import io.deepsense.deeplang.doperables.serialization.SerializableSparkModel
import io.deepsense.deeplang.doperables.spark.wrappers.params.common.ProbabilisticClassifierParams
import io.deepsense.deeplang.params.Param

class NaiveBayesModel
    extends SparkModelWrapper[SparkNaiveBayesModel, SparkNaiveBayes]
    with ProbabilisticClassifierParams {

  override val params: Array[Param[_]] = Array(featuresColumn, probabilityColumn, rawPredictionColumn, predictionColumn)

  override def report: Report = {
    val pi = SparkSummaryEntry(
      name = "pi",
      value = sparkModel.pi,
      description = "Log of class priors, whose dimension is C (number of classes)"
    )

    val theta = SparkSummaryEntry(
      name = "theta",
      value = sparkModel.theta,
      description = "Log of class conditional probabilities, " +
        "whose dimension is C (number of classes) by D (number of features)"
    )

    super.report
      .withAdditionalTable(CommonTablesGenerators.modelSummary(List(pi) ++ List(theta)))
  }

  override protected def loadModel(ctx: ExecutionContext, path: String): SerializableSparkModel[SparkNaiveBayesModel] =
    new SerializableSparkModel(SparkNaiveBayesModel.load(path))

}
