package ai.deepsense.deeplang.actionobjects.spark.wrappers.models

import org.apache.spark.ml.recommendation.{ALS => SparkALS}
import org.apache.spark.ml.recommendation.{ALSModel => SparkALSModel}

import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.actionobjects.SparkModelWrapper
import ai.deepsense.deeplang.actionobjects.report.CommonTablesGenerators.SparkSummaryEntry
import ai.deepsense.deeplang.actionobjects.report.CommonTablesGenerators
import ai.deepsense.deeplang.actionobjects.report.Report
import ai.deepsense.deeplang.actionobjects.serialization.SerializableSparkModel
import ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common.HasItemColumnParam
import ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common.HasPredictionColumnCreatorParam
import ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common.HasUserColumnParam
import ai.deepsense.deeplang.parameters.Parameter

class ALSModel
    extends SparkModelWrapper[SparkALSModel, SparkALS]
    with HasItemColumnParam
    with HasPredictionColumnCreatorParam
    with HasUserColumnParam {

  override val params: Array[Parameter[_]] = Array(itemColumn, predictionColumn, userColumn)

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
