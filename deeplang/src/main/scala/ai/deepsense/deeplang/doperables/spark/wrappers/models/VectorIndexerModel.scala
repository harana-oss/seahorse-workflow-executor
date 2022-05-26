package ai.deepsense.deeplang.doperables.spark.wrappers.models

import org.apache.spark.ml.feature.{VectorIndexer => SparkVectorIndexer}
import org.apache.spark.ml.feature.{VectorIndexerModel => SparkVectorIndexerModel}

import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.doperables.SparkSingleColumnModelWrapper
import ai.deepsense.deeplang.doperables.report.CommonTablesGenerators
import ai.deepsense.deeplang.doperables.report.Report
import ai.deepsense.deeplang.doperables.serialization.SerializableSparkModel
import ai.deepsense.deeplang.params.Param

class VectorIndexerModel extends SparkSingleColumnModelWrapper[SparkVectorIndexerModel, SparkVectorIndexer] {

  override protected def getSpecificParams: Array[Param[_]] = Array()

  override def report(extended: Boolean = true): Report =
    super.report(extended).withAdditionalTable(CommonTablesGenerators.categoryMaps(sparkModel.categoryMaps))

  override protected def loadModel(
      ctx: ExecutionContext,
      path: String
  ): SerializableSparkModel[SparkVectorIndexerModel] =
    new SerializableSparkModel(SparkVectorIndexerModel.load(path))

}
