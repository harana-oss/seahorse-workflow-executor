package ai.deepsense.deeplang.actionobjects.spark.wrappers.models

import org.apache.spark.ml.feature.{IDF => SparkIDF}
import org.apache.spark.ml.feature.{IDFModel => SparkIDFModel}

import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.actionobjects.SparkSingleColumnModelWrapper
import ai.deepsense.deeplang.actionobjects.report.CommonTablesGenerators.SparkSummaryEntry
import ai.deepsense.deeplang.actionobjects.report.CommonTablesGenerators
import ai.deepsense.deeplang.actionobjects.report.Report
import ai.deepsense.deeplang.actionobjects.serialization.SerializableSparkModel
import ai.deepsense.deeplang.parameters.Parameter

class IDFModel extends SparkSingleColumnModelWrapper[SparkIDFModel, SparkIDF] {

  override protected def getSpecificParams: Array[Parameter[_]] = Array()

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
