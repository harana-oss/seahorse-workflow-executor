package ai.deepsense.deeplang.doperables.spark.wrappers.models

import org.apache.spark.ml.feature.{Word2Vec => SparkWord2Vec}
import org.apache.spark.ml.feature.{Word2VecModel => SparkWord2VecModel}

import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.doperables.SparkSingleColumnModelWrapper
import ai.deepsense.deeplang.doperables.serialization.SerializableSparkModel
import ai.deepsense.deeplang.doperables.spark.wrappers.params.Word2VecParams
import ai.deepsense.deeplang.params.Param

class Word2VecModel extends SparkSingleColumnModelWrapper[SparkWord2VecModel, SparkWord2Vec] with Word2VecParams {

  override protected def getSpecificParams: Array[Param[_]] =
    Array(maxIterations, stepSize, seed, vectorSize, numPartitions, minCount)

  override protected def loadModel(ctx: ExecutionContext, path: String): SerializableSparkModel[SparkWord2VecModel] =
    new SerializableSparkModel(SparkWord2VecModel.load(path))

}
