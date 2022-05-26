package ai.deepsense.deeplang.doperables.spark.wrappers.models

import scala.language.reflectiveCalls

import org.apache.spark.ml.feature.{Bucketizer => SparkQuantileDiscretizerModel}
import org.apache.spark.ml.feature.{QuantileDiscretizer => SparkQuantileDiscretizer}

import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.doperables.SparkSingleColumnModelWrapper
import ai.deepsense.deeplang.doperables.report.CommonTablesGenerators.SparkSummaryEntry
import ai.deepsense.deeplang.doperables.report.CommonTablesGenerators
import ai.deepsense.deeplang.doperables.report.Report
import ai.deepsense.deeplang.doperables.serialization.SerializableSparkModel
import ai.deepsense.deeplang.params.Param

class QuantileDiscretizerModel
    extends SparkSingleColumnModelWrapper[SparkQuantileDiscretizerModel, SparkQuantileDiscretizer] {

  /*
   * Parameter `splits` is not wrapped because it violates a design assumption, that every parameter
   * of a model is also a parameter of a corresponding estimator.
   */
  override protected def getSpecificParams: Array[Param[_]] = Array()

  override def report(extended: Boolean = true): Report = {
    val summary =
      List(
        SparkSummaryEntry(
          name = "splits",
          value = sparkModel.getSplits,
          description = "Split points for mapping continuous features into buckets."
        )
      )

    super
      .report(extended)
      .withAdditionalTable(CommonTablesGenerators.modelSummary(summary))
  }

  override protected def loadModel(
      ctx: ExecutionContext,
      path: String
  ): SerializableSparkModel[SparkQuantileDiscretizerModel] =
    new SerializableSparkModel(SparkQuantileDiscretizerModel.load(path))

}
