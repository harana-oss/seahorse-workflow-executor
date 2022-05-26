package io.deepsense.deeplang.doperables.spark.wrappers.models

import org.apache.spark.ml.clustering.{KMeans => SparkKMeans}
import org.apache.spark.ml.clustering.{KMeansModel => SparkKMeansModel}

import io.deepsense.deeplang.ExecutionContext
import io.deepsense.deeplang.doperables.SparkModelWrapper
import io.deepsense.deeplang.doperables.report.CommonTablesGenerators.SparkSummaryEntry
import io.deepsense.deeplang.doperables.report.CommonTablesGenerators
import io.deepsense.deeplang.doperables.report.Report
import io.deepsense.deeplang.doperables.serialization.SerializableSparkModel
import io.deepsense.deeplang.params.Param

class KMeansModel extends SparkModelWrapper[SparkKMeansModel, SparkKMeans] {

  override val params: Array[Param[_]] = Array()

  override def report: Report = {
    val summary =
      List(
        SparkSummaryEntry(
          name = "cluster centers",
          value = sparkModel.clusterCenters,
          description = "Positions of cluster centers."
        )
      )

    super.report
      .withAdditionalTable(CommonTablesGenerators.modelSummary(summary))
  }

  override protected def loadModel(ctx: ExecutionContext, path: String): SerializableSparkModel[SparkKMeansModel] =
    new SerializableSparkModel(SparkKMeansModel.load(path))

}
