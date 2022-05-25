package io.deepsense.deeplang.doperables.spark.wrappers.models

import org.apache.spark.ml.feature.{ChiSqSelector => SparkChiSqSelector, ChiSqSelectorModel => SparkChiSqSelectorModel}

import io.deepsense.deeplang.ExecutionContext
import io.deepsense.deeplang.doperables.SparkModelWrapper
import io.deepsense.deeplang.doperables.report.CommonTablesGenerators.SparkSummaryEntry
import io.deepsense.deeplang.doperables.report.{CommonTablesGenerators, Report}
import io.deepsense.deeplang.doperables.serialization.SerializableSparkModel
import io.deepsense.deeplang.doperables.spark.wrappers.params.common.{HasFeaturesColumnParam, HasLabelColumnParam, HasOutputColumn}
import io.deepsense.deeplang.params.Param

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

  override def report: Report = {
    val summary =
      List(
        SparkSummaryEntry(
          name = "selected features",
          value = sparkModel.selectedFeatures,
          description = "List of indices to select."))

    super.report
      .withAdditionalTable(CommonTablesGenerators.modelSummary(summary))
  }

  override protected def loadModel(
      ctx: ExecutionContext,
      path: String): SerializableSparkModel[SparkChiSqSelectorModel] = {
    new SerializableSparkModel(SparkChiSqSelectorModel.load(path))
  }
}
