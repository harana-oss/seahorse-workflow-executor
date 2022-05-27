package ai.deepsense.deeplang.actionobjects.spark.wrappers.models

import org.apache.spark.ml.feature.{VectorIndexer => SparkVectorIndexer}
import org.apache.spark.ml.feature.{VectorIndexerModel => SparkVectorIndexerModel}

import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.actionobjects.SparkSingleColumnModelWrapper
import ai.deepsense.deeplang.actionobjects.report.CommonTablesGenerators
import ai.deepsense.deeplang.actionobjects.report.Report
import ai.deepsense.deeplang.actionobjects.serialization.SerializableSparkModel
import ai.deepsense.deeplang.parameters.Parameter

class VectorIndexerModel extends SparkSingleColumnModelWrapper[SparkVectorIndexerModel, SparkVectorIndexer] {

  override protected def getSpecificParams: Array[Parameter[_]] = Array()

  override def report(extended: Boolean = true): Report =
    super.report(extended).withAdditionalTable(CommonTablesGenerators.categoryMaps(sparkModel.categoryMaps))

  override protected def loadModel(
      ctx: ExecutionContext,
      path: String
  ): SerializableSparkModel[SparkVectorIndexerModel] =
    new SerializableSparkModel(SparkVectorIndexerModel.load(path))

}
