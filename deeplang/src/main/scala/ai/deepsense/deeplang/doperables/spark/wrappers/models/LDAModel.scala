package ai.deepsense.deeplang.doperables.spark.wrappers.models

import org.apache.spark.ml.clustering.DistributedLDAModel
import org.apache.spark.ml.clustering.LocalLDAModel
import org.apache.spark.ml.clustering.{LDA => SparkLDA}
import org.apache.spark.ml.clustering.{LDAModel => SparkLDAModel}

import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.doperables.SparkModelWrapper
import ai.deepsense.deeplang.doperables.report.CommonTablesGenerators.SparkSummaryEntry
import ai.deepsense.deeplang.doperables.report.CommonTablesGenerators
import ai.deepsense.deeplang.doperables.report.Report
import ai.deepsense.deeplang.doperables.serialization.SerializableSparkModel
import ai.deepsense.deeplang.doperables.spark.wrappers.params.common.HasFeaturesColumnParam
import ai.deepsense.deeplang.doperables.spark.wrappers.params.common.HasSeedParam

class LDAModel extends SparkModelWrapper[SparkLDAModel, SparkLDA] with HasFeaturesColumnParam with HasSeedParam {

  val params: Array[ai.deepsense.deeplang.params.Param[_]] = Array(featuresColumn, seed)

  override def report(extended: Boolean = true): Report = {
    val vocabularySize =
      SparkSummaryEntry(
        name = "vocabulary size",
        value = sparkModel.vocabSize,
        description = "The number of terms in the vocabulary."
      )

    val estimatedDocConcentration =
      SparkSummaryEntry(
        name = "estimated doc concentration",
        value = sparkModel.estimatedDocConcentration,
        description = "Value for `doc concentration` estimated from data."
      )

    super
      .report(extended)
      .withAdditionalTable(CommonTablesGenerators.modelSummary(List(vocabularySize, estimatedDocConcentration)))
  }

  override protected def loadModel(ctx: ExecutionContext, path: String): SerializableSparkModel[SparkLDAModel] = {
    try
      new SerializableSparkModel(LocalLDAModel.load(path))
    catch {
      case e: IllegalArgumentException =>
        logger.warn(s"LocalLDAModel.load($path) failed. Trying to load DistributedLDAModel.", e)
        new SerializableSparkModel(DistributedLDAModel.load(path))
    }
  }

}
