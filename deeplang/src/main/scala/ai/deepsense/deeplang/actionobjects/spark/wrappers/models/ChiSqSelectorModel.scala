package ai.deepsense.deeplang.actionobjects.spark.wrappers.models

import org.apache.spark.ml.feature.{ChiSqSelector => SparkChiSqSelector}
import org.apache.spark.ml.feature.{ChiSqSelectorModel => SparkChiSqSelectorModel}

import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.actionobjects.SparkModelWrapper
import ai.deepsense.deeplang.actionobjects.report.CommonTablesGenerators.SparkSummaryEntry
import ai.deepsense.deeplang.actionobjects.report.CommonTablesGenerators
import ai.deepsense.deeplang.actionobjects.report.Report
import ai.deepsense.deeplang.actionobjects.serialization.SerializableSparkModel
import ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common.HasFeaturesColumnParam
import ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common.HasLabelColumnParam
import ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common.HasOutputColumn
import ai.deepsense.deeplang.parameters.Parameter

class ChiSqSelectorModel
    extends SparkModelWrapper[SparkChiSqSelectorModel, SparkChiSqSelector]
    with HasFeaturesColumnParam
    with HasOutputColumn
    with HasLabelColumnParam {

  override val params: Array[Parameter[_]] = Array(
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
