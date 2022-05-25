package io.deepsense.deeplang.doperables.spark.wrappers.models

import scala.language.reflectiveCalls

import org.apache.spark.ml.feature.{Bucketizer => SparkQuantileDiscretizerModel, QuantileDiscretizer => SparkQuantileDiscretizer}

import io.deepsense.deeplang.ExecutionContext
import io.deepsense.deeplang.doperables.SparkSingleColumnModelWrapper
import io.deepsense.deeplang.doperables.report.CommonTablesGenerators.SparkSummaryEntry
import io.deepsense.deeplang.doperables.report.{CommonTablesGenerators, Report}
import io.deepsense.deeplang.doperables.serialization.SerializableSparkModel
import io.deepsense.deeplang.params.Param

class QuantileDiscretizerModel
  extends SparkSingleColumnModelWrapper[SparkQuantileDiscretizerModel, SparkQuantileDiscretizer] {

  /*
   * Parameter `splits` is not wrapped because it violates a design assumption, that every parameter
   * of a model is also a parameter of a corresponding estimator.
   */
  override protected def getSpecificParams: Array[Param[_]] = Array()

  override def report: Report = {
    val summary =
      List(
        SparkSummaryEntry(
          name = "splits",
          value = sparkModel.getSplits,
          description = "Split points for mapping continuous features into buckets."))

    super.report
      .withAdditionalTable(CommonTablesGenerators.modelSummary(summary))
  }

  override protected def loadModel(
      ctx: ExecutionContext,
      path: String): SerializableSparkModel[SparkQuantileDiscretizerModel] = {
    new SerializableSparkModel(SparkQuantileDiscretizerModel.load(path))
  }

}
