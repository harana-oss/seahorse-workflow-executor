package ai.deepsense.deeplang.doperables.spark.wrappers.models

import org.apache.spark.ml.clustering.{KMeans => SparkKMeans}
import org.apache.spark.ml.clustering.{KMeansModel => SparkKMeansModel}

import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.doperables.SparkModelWrapper
import ai.deepsense.deeplang.doperables.report.CommonTablesGenerators.SparkSummaryEntry
import ai.deepsense.deeplang.doperables.report.CommonTablesGenerators
import ai.deepsense.deeplang.doperables.report.Report
import ai.deepsense.deeplang.doperables.serialization.SerializableSparkModel
import ai.deepsense.deeplang.params.Param

class KMeansModel extends SparkModelWrapper[SparkKMeansModel, SparkKMeans] {

  override val params: Array[Param[_]] = Array()

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
