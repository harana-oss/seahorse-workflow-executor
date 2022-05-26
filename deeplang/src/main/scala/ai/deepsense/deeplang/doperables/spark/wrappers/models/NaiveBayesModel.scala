package ai.deepsense.deeplang.doperables.spark.wrappers.models

import org.apache.spark.ml.classification.{NaiveBayes => SparkNaiveBayes}
import org.apache.spark.ml.classification.{NaiveBayesModel => SparkNaiveBayesModel}

import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.doperables.SparkModelWrapper
import ai.deepsense.deeplang.doperables.report.CommonTablesGenerators.SparkSummaryEntry
import ai.deepsense.deeplang.doperables.report.CommonTablesGenerators
import ai.deepsense.deeplang.doperables.report.Report
import ai.deepsense.deeplang.doperables.serialization.SerializableSparkModel
import ai.deepsense.deeplang.doperables.spark.wrappers.params.common.ProbabilisticClassifierParams
import ai.deepsense.deeplang.params.Param

class NaiveBayesModel
    extends SparkModelWrapper[SparkNaiveBayesModel, SparkNaiveBayes]
    with ProbabilisticClassifierParams {

  override val params: Array[Param[_]] = Array(featuresColumn, probabilityColumn, rawPredictionColumn, predictionColumn)

  override def report(extended: Boolean = true): Report = {
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

    super
      .report(extended)
      .withAdditionalTable(CommonTablesGenerators.modelSummary(List(pi) ++ List(theta)))
  }

  override protected def loadModel(ctx: ExecutionContext, path: String): SerializableSparkModel[SparkNaiveBayesModel] =
    new SerializableSparkModel(SparkNaiveBayesModel.load(path))

}
