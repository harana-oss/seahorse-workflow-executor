package io.deepsense.deeplang.doperables.spark.wrappers.models

import org.apache.spark.ml.feature.{StandardScaler => SparkStandardScaler}
import org.apache.spark.ml.feature.{StandardScalerModel => SparkStandardScalerModel}

import io.deepsense.deeplang.ExecutionContext
import io.deepsense.deeplang.doperables.SparkSingleColumnModelWrapper
import io.deepsense.deeplang.doperables.report.CommonTablesGenerators.SparkSummaryEntry
import io.deepsense.deeplang.doperables.report.CommonTablesGenerators
import io.deepsense.deeplang.doperables.report.Report
import io.deepsense.deeplang.doperables.serialization.SerializableSparkModel
import io.deepsense.deeplang.params.Param

class StandardScalerModel extends SparkSingleColumnModelWrapper[SparkStandardScalerModel, SparkStandardScaler] {

  override def convertInputNumericToVector: Boolean = true

  override def convertOutputVectorToDouble: Boolean = true

  override protected def getSpecificParams: Array[Param[_]] = Array()

  override def report: Report = {
    val summary =
      List(
        SparkSummaryEntry(
          name = "std",
          value = sparkModel.std,
          description = "Vector of standard deviations of the model."
        ),
        SparkSummaryEntry(name = "mean", value = sparkModel.mean, description = "Vector of means of the model.")
      )

    super.report
      .withAdditionalTable(CommonTablesGenerators.modelSummary(summary))
  }

  override protected def loadModel(
      ctx: ExecutionContext,
      path: String
  ): SerializableSparkModel[SparkStandardScalerModel] =
    new SerializableSparkModel(SparkStandardScalerModel.load(path))

}
