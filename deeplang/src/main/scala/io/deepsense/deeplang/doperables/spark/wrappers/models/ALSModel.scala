package io.deepsense.deeplang.doperables.spark.wrappers.models

import org.apache.spark.ml.recommendation.{ALS => SparkALS}
import org.apache.spark.ml.recommendation.{ALSModel => SparkALSModel}

import io.deepsense.deeplang.ExecutionContext
import io.deepsense.deeplang.doperables.SparkModelWrapper
import io.deepsense.deeplang.doperables.report.CommonTablesGenerators.SparkSummaryEntry
import io.deepsense.deeplang.doperables.report.CommonTablesGenerators
import io.deepsense.deeplang.doperables.report.Report
import io.deepsense.deeplang.doperables.serialization.SerializableSparkModel
import io.deepsense.deeplang.doperables.spark.wrappers.params.common.HasItemColumnParam
import io.deepsense.deeplang.doperables.spark.wrappers.params.common.HasPredictionColumnCreatorParam
import io.deepsense.deeplang.doperables.spark.wrappers.params.common.HasUserColumnParam
import io.deepsense.deeplang.params.Param

class ALSModel
    extends SparkModelWrapper[SparkALSModel, SparkALS]
    with HasItemColumnParam
    with HasPredictionColumnCreatorParam
    with HasUserColumnParam {

  override val params: Array[Param[_]] = Array(itemColumn, predictionColumn, userColumn)

  override def report: Report = {
    val summary =
      List(
        SparkSummaryEntry(
          name = "rank",
          value = sparkModel.rank,
          description = "Rank of the matrix factorization model."
        )
      )

    super.report
      .withAdditionalTable(CommonTablesGenerators.modelSummary(summary))
  }

  override protected def loadModel(ctx: ExecutionContext, path: String): SerializableSparkModel[SparkALSModel] =
    new SerializableSparkModel(SparkALSModel.load(path))

}
