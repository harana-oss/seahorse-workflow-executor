package ai.deepsense.deeplang.doperables.spark.wrappers.models

import org.apache.spark.ml.recommendation.{ALS => SparkALS}
import org.apache.spark.ml.recommendation.{ALSModel => SparkALSModel}

import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.doperables.SparkModelWrapper
import ai.deepsense.deeplang.doperables.report.CommonTablesGenerators.SparkSummaryEntry
import ai.deepsense.deeplang.doperables.report.CommonTablesGenerators
import ai.deepsense.deeplang.doperables.report.Report
import ai.deepsense.deeplang.doperables.serialization.SerializableSparkModel
import ai.deepsense.deeplang.doperables.spark.wrappers.params.common.HasItemColumnParam
import ai.deepsense.deeplang.doperables.spark.wrappers.params.common.HasPredictionColumnCreatorParam
import ai.deepsense.deeplang.doperables.spark.wrappers.params.common.HasUserColumnParam
import ai.deepsense.deeplang.params.Param

class ALSModel
    extends SparkModelWrapper[SparkALSModel, SparkALS]
    with HasItemColumnParam
    with HasPredictionColumnCreatorParam
    with HasUserColumnParam {

  override val params: Array[Param[_]] = Array(itemColumn, predictionColumn, userColumn)

  override def report(extended: Boolean = true): Report = {
    val summary =
      List(
        SparkSummaryEntry(
          name = "rank",
          value = sparkModel.rank,
          description = "Rank of the matrix factorization model."
        )
      )

    super
      .report(extended)
      .withAdditionalTable(CommonTablesGenerators.modelSummary(summary))
  }

  override protected def loadModel(ctx: ExecutionContext, path: String): SerializableSparkModel[SparkALSModel] =
    new SerializableSparkModel(SparkALSModel.load(path))

}
