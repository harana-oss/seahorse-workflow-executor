package ai.deepsense.deeplang.actionobjects.spark.wrappers.estimators

import org.apache.spark.ml.feature.{Word2Vec => SparkWord2Vec}
import org.apache.spark.ml.feature.{Word2VecModel => SparkWord2VecModel}

import ai.deepsense.deeplang.actionobjects.SparkSingleColumnEstimatorWrapper
import ai.deepsense.deeplang.actionobjects.spark.wrappers.models.Word2VecModel
import ai.deepsense.deeplang.actionobjects.spark.wrappers.params.Word2VecParams
import ai.deepsense.deeplang.parameters.Parameter

class Word2VecEstimator
    extends SparkSingleColumnEstimatorWrapper[SparkWord2VecModel, SparkWord2Vec, Word2VecModel]
    with Word2VecParams {

  override lazy val stepSizeDefault = 0.025

  override lazy val maxIterationsDefault = 1.0

  override protected def getSpecificParams: Array[Parameter[_]] =
    Array(maxIterations, stepSize, seed, vectorSize, numPartitions, minCount)

}
