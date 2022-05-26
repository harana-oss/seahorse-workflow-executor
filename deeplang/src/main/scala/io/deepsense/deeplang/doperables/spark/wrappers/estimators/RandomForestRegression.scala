package io.deepsense.deeplang.doperables.spark.wrappers.estimators

import org.apache.spark.ml.regression.{RandomForestRegressionModel => SparkRFRModel}
import org.apache.spark.ml.regression.{RandomForestRegressor => SparkRFR}

import io.deepsense.commons.utils.Logging
import io.deepsense.deeplang.doperables.SparkEstimatorWrapper
import io.deepsense.deeplang.doperables.spark.wrappers.models.RandomForestRegressionModel
import io.deepsense.deeplang.doperables.spark.wrappers.params.common._
import io.deepsense.deeplang.params.Param
import io.deepsense.deeplang.params.choice.Choice
import io.deepsense.deeplang.params.validators.RangeValidator
import io.deepsense.deeplang.params.wrappers.spark._

class RandomForestRegression
    extends SparkEstimatorWrapper[SparkRFRModel, SparkRFR, RandomForestRegressionModel]
    with PredictorParams
    with HasLabelColumnParam
    with HasSeedParam
    with HasMaxDepthParam
    with HasMinInstancePerNodeParam
    with HasMaxBinsParam
    with HasSubsamplingRateParam
    with HasMinInfoGainParam
    with HasMaxMemoryInMBParam
    with HasCacheNodeIdsParam
    with HasCheckpointIntervalParam
    with HasNumTreesParam
    with HasFeatureSubsetStrategyParam
    with HasRegressionImpurityParam
    with Logging {

  override val params: Array[Param[_]] = Array(
    maxDepth, maxBins, minInstancesPerNode, minInfoGain, maxMemoryInMB, cacheNodeIds, checkpointInterval, impurity,
    subsamplingRate, seed, numTrees, featureSubsetStrategy, labelColumn, featuresColumn, predictionColumn
  )

}
