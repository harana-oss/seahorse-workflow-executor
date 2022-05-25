package io.deepsense.deeplang.doperables.spark.wrappers.models

import org.apache.spark.ml.feature.{Word2Vec => SparkWord2Vec, Word2VecModel => SparkWord2VecModel}

import io.deepsense.deeplang.ExecutionContext
import io.deepsense.deeplang.doperables.SparkSingleColumnModelWrapper
import io.deepsense.deeplang.doperables.serialization.SerializableSparkModel
import io.deepsense.deeplang.doperables.spark.wrappers.params.Word2VecParams
import io.deepsense.deeplang.params.Param

class Word2VecModel
  extends SparkSingleColumnModelWrapper[SparkWord2VecModel, SparkWord2Vec]
  with Word2VecParams {

  override protected def getSpecificParams: Array[Param[_]] = Array(
    maxIterations,
    stepSize,
    seed,
    vectorSize,
    numPartitions,
    minCount)

  override protected def loadModel(
      ctx: ExecutionContext,
      path: String): SerializableSparkModel[SparkWord2VecModel] = {
    new SerializableSparkModel(SparkWord2VecModel.load(path))
  }
}
