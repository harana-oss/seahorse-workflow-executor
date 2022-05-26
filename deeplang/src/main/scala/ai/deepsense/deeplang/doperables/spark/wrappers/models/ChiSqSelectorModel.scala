package ai.deepsense.deeplang.doperables.spark.wrappers.models

import org.apache.spark.ml.feature.{ChiSqSelector => SparkChiSqSelector}
import org.apache.spark.ml.feature.{ChiSqSelectorModel => SparkChiSqSelectorModel}

import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.doperables.SparkModelWrapper
import ai.deepsense.deeplang.doperables.report.CommonTablesGenerators.SparkSummaryEntry
import ai.deepsense.deeplang.doperables.report.CommonTablesGenerators
import ai.deepsense.deeplang.doperables.report.Report
import ai.deepsense.deeplang.doperables.serialization.SerializableSparkModel
import ai.deepsense.deeplang.doperables.spark.wrappers.params.common.HasFeaturesColumnParam
import ai.deepsense.deeplang.doperables.spark.wrappers.params.common.HasLabelColumnParam
import ai.deepsense.deeplang.doperables.spark.wrappers.params.common.HasOutputColumn
import ai.deepsense.deeplang.params.Param

class ChiSqSelectorModel
    extends SparkModelWrapper[SparkChiSqSelectorModel, SparkChiSqSelector]
    with HasFeaturesColumnParam
    with HasOutputColumn
    with HasLabelColumnParam {

  override val params: Array[Param[_]] = Array(
    featuresColumn,
    outputColumn,
    labelColumn
  )

  override def report(extended: Boolean = true): Report = {
    val summary =
      List(
        SparkSummaryEntry(
          name = "selected features",
          value = sparkModel.selectedFeatures,
          description = "List of indices to select."
        )
      )

    super
      .report(extended)
      .withAdditionalTable(CommonTablesGenerators.modelSummary(summary))
  }

  override protected def loadModel(
      ctx: ExecutionContext,
      path: String
  ): SerializableSparkModel[SparkChiSqSelectorModel] =
    new SerializableSparkModel(SparkChiSqSelectorModel.load(path))

}
