package io.deepsense.deeplang.doperables.spark.wrappers.models

import org.apache.spark.ml.feature.{VectorIndexer => SparkVectorIndexer}
import org.apache.spark.ml.feature.{VectorIndexerModel => SparkVectorIndexerModel}

import io.deepsense.deeplang.ExecutionContext
import io.deepsense.deeplang.doperables.SparkSingleColumnModelWrapper
import io.deepsense.deeplang.doperables.report.CommonTablesGenerators
import io.deepsense.deeplang.doperables.report.Report
import io.deepsense.deeplang.doperables.serialization.SerializableSparkModel
import io.deepsense.deeplang.params.Param

class VectorIndexerModel extends SparkSingleColumnModelWrapper[SparkVectorIndexerModel, SparkVectorIndexer] {

  override protected def getSpecificParams: Array[Param[_]] = Array()

  override def report: Report =
    super.report.withAdditionalTable(CommonTablesGenerators.categoryMaps(sparkModel.categoryMaps))

  override protected def loadModel(
      ctx: ExecutionContext,
      path: String
  ): SerializableSparkModel[SparkVectorIndexerModel] =
    new SerializableSparkModel(SparkVectorIndexerModel.load(path))

}
