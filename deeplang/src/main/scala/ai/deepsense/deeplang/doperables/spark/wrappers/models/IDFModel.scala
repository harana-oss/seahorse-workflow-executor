package ai.deepsense.deeplang.doperables.spark.wrappers.models

import org.apache.spark.ml.feature.{IDF => SparkIDF}
import org.apache.spark.ml.feature.{IDFModel => SparkIDFModel}

import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.doperables.SparkSingleColumnModelWrapper
import ai.deepsense.deeplang.doperables.report.CommonTablesGenerators.SparkSummaryEntry
import ai.deepsense.deeplang.doperables.report.CommonTablesGenerators
import ai.deepsense.deeplang.doperables.report.Report
import ai.deepsense.deeplang.doperables.serialization.SerializableSparkModel
import ai.deepsense.deeplang.params.Param

class IDFModel extends SparkSingleColumnModelWrapper[SparkIDFModel, SparkIDF] {

  override protected def getSpecificParams: Array[Param[_]] = Array()

  override def report(extended: Boolean = true): Report = {
    val summary =
      List(
        SparkSummaryEntry(
          name = "IDF vector",
          value = sparkModel.idf,
          description = "The inverse document frequency vector."
        )
      )

    super
      .report(extended)
      .withAdditionalTable(CommonTablesGenerators.modelSummary(summary))
  }

  override protected def loadModel(ctx: ExecutionContext, path: String): SerializableSparkModel[SparkIDFModel] =
    new SerializableSparkModel(SparkIDFModel.load(path))

}
