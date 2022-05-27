package ai.deepsense.deeplang.actionobjects.spark.wrappers.models

import org.apache.spark.ml.feature.{MinMaxScaler => SparkMinMaxScaler}
import org.apache.spark.ml.feature.{MinMaxScalerModel => SparkMinMaxScalerModel}

import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.actionobjects.SparkSingleColumnModelWrapper
import ai.deepsense.deeplang.actionobjects.report.CommonTablesGenerators.SparkSummaryEntry
import ai.deepsense.deeplang.actionobjects.report.CommonTablesGenerators
import ai.deepsense.deeplang.actionobjects.report.Report
import ai.deepsense.deeplang.actionobjects.serialization.SerializableSparkModel
import ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common.MinMaxParams
import ai.deepsense.deeplang.parameters.Parameter

class MinMaxScalerModel
    extends SparkSingleColumnModelWrapper[SparkMinMaxScalerModel, SparkMinMaxScaler]
    with MinMaxParams {

  override def convertInputNumericToVector: Boolean = true

  override def convertOutputVectorToDouble: Boolean = true

  override protected def getSpecificParams: Array[Parameter[_]] = Array(min, max)

  override def report(extended: Boolean = true): Report = {
    val summary =
      List(
        SparkSummaryEntry(
          name = "original min",
          value = sparkModel.originalMin,
          description = "Minimal value for each original column during fitting."
        ),
        SparkSummaryEntry(
          name = "original max",
          value = sparkModel.originalMax,
          description = "Maximum value for each original column during fitting."
        )
      )

    super
      .report(extended)
      .withAdditionalTable(CommonTablesGenerators.modelSummary(summary))
  }

  override protected def loadModel(
      ctx: ExecutionContext,
      path: String
  ): SerializableSparkModel[SparkMinMaxScalerModel] =
    new SerializableSparkModel(SparkMinMaxScalerModel.load(path))

}
