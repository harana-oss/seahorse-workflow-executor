package io.deepsense.deeplang.doperables.spark.wrappers.estimators

import org.apache.spark.ml.feature.{Word2Vec => SparkWord2Vec, Word2VecModel => SparkWord2VecModel}

import io.deepsense.deeplang.doperables.SparkSingleColumnEstimatorWrapper
import io.deepsense.deeplang.doperables.spark.wrappers.models.Word2VecModel
import io.deepsense.deeplang.doperables.spark.wrappers.params.Word2VecParams
import io.deepsense.deeplang.params.Param

class Word2VecEstimator
  extends SparkSingleColumnEstimatorWrapper[SparkWord2VecModel, SparkWord2Vec, Word2VecModel]
  with Word2VecParams {

  override lazy val stepSizeDefault = 0.025
  override lazy val maxIterationsDefault = 1.0

  override protected def getSpecificParams: Array[Param[_]] = Array(
    maxIterations,
    stepSize,
    seed,
    vectorSize,
    numPartitions,
    minCount)
}
