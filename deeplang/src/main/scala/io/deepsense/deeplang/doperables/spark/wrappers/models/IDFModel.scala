package io.deepsense.deeplang.doperables.spark.wrappers.models

import org.apache.spark.ml.feature.{IDF => SparkIDF}
import org.apache.spark.ml.feature.{IDFModel => SparkIDFModel}

import io.deepsense.deeplang.ExecutionContext
import io.deepsense.deeplang.doperables.SparkSingleColumnModelWrapper
import io.deepsense.deeplang.doperables.report.CommonTablesGenerators.SparkSummaryEntry
import io.deepsense.deeplang.doperables.report.CommonTablesGenerators
import io.deepsense.deeplang.doperables.report.Report
import io.deepsense.deeplang.doperables.serialization.SerializableSparkModel
import io.deepsense.deeplang.params.Param

class IDFModel extends SparkSingleColumnModelWrapper[SparkIDFModel, SparkIDF] {

  override protected def getSpecificParams: Array[Param[_]] = Array()

  override def report: Report = {
    val summary =
      List(
        SparkSummaryEntry(
          name = "IDF vector",
          value = sparkModel.idf,
          description = "The inverse document frequency vector."
        )
      )

    super.report
      .withAdditionalTable(CommonTablesGenerators.modelSummary(summary))
  }

  override protected def loadModel(ctx: ExecutionContext, path: String): SerializableSparkModel[SparkIDFModel] =
    new SerializableSparkModel(SparkIDFModel.load(path))

}
