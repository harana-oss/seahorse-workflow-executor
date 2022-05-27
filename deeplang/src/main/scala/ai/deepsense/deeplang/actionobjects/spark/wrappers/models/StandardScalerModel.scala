package ai.deepsense.deeplang.actionobjects.spark.wrappers.models

import org.apache.spark.ml.feature.{StandardScaler => SparkStandardScaler}
import org.apache.spark.ml.feature.{StandardScalerModel => SparkStandardScalerModel}

import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.actionobjects.SparkSingleColumnModelWrapper
import ai.deepsense.deeplang.actionobjects.report.CommonTablesGenerators.SparkSummaryEntry
import ai.deepsense.deeplang.actionobjects.report.CommonTablesGenerators
import ai.deepsense.deeplang.actionobjects.report.Report
import ai.deepsense.deeplang.actionobjects.serialization.SerializableSparkModel
import ai.deepsense.deeplang.parameters.Parameter

class StandardScalerModel extends SparkSingleColumnModelWrapper[SparkStandardScalerModel, SparkStandardScaler] {

  override def convertInputNumericToVector: Boolean = true

  override def convertOutputVectorToDouble: Boolean = true

  override protected def getSpecificParams: Array[Parameter[_]] = Array()

  override def report(extended: Boolean = true): Report = {
    val summary =
      List(
        SparkSummaryEntry(
          name = "std",
          value = sparkModel.std,
          description = "Vector of standard deviations of the model."
        ),
        SparkSummaryEntry(name = "mean", value = sparkModel.mean, description = "Vector of means of the model.")
      )

    super
      .report(extended)
      .withAdditionalTable(CommonTablesGenerators.modelSummary(summary))
  }

  override protected def loadModel(
      ctx: ExecutionContext,
      path: String
  ): SerializableSparkModel[SparkStandardScalerModel] =
    new SerializableSparkModel(SparkStandardScalerModel.load(path))

}
