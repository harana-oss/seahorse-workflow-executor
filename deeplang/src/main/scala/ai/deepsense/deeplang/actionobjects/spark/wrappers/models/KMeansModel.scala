package ai.deepsense.deeplang.actionobjects.spark.wrappers.models

import org.apache.spark.ml.clustering.{KMeans => SparkKMeans}
import org.apache.spark.ml.clustering.{KMeansModel => SparkKMeansModel}

import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.actionobjects.SparkModelWrapper
import ai.deepsense.deeplang.actionobjects.report.CommonTablesGenerators.SparkSummaryEntry
import ai.deepsense.deeplang.actionobjects.report.CommonTablesGenerators
import ai.deepsense.deeplang.actionobjects.report.Report
import ai.deepsense.deeplang.actionobjects.serialization.SerializableSparkModel
import ai.deepsense.deeplang.parameters.Parameter

class KMeansModel extends SparkModelWrapper[SparkKMeansModel, SparkKMeans] {

  override val params: Array[Parameter[_]] = Array()

  override def report(extended: Boolean = true): Report = {
    val summary =
      List(
        SparkSummaryEntry(
          name = "cluster centers",
          value = sparkModel.clusterCenters,
          description = "Positions of cluster centers."
        )
      )

    super
      .report(extended)
      .withAdditionalTable(CommonTablesGenerators.modelSummary(summary))
  }

  override protected def loadModel(ctx: ExecutionContext, path: String): SerializableSparkModel[SparkKMeansModel] =
    new SerializableSparkModel(SparkKMeansModel.load(path))

}
