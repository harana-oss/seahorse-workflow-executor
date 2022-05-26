package ai.deepsense.deeplang.doperables.spark.wrappers.estimators

import org.apache.spark.ml.regression.{RandomForestRegressionModel => SparkRFRModel}
import org.apache.spark.ml.regression.{RandomForestRegressor => SparkRFR}

import ai.deepsense.commons.utils.Logging
import ai.deepsense.deeplang.doperables.SparkEstimatorWrapper
import ai.deepsense.deeplang.doperables.spark.wrappers.models.RandomForestRegressionModel
import ai.deepsense.deeplang.doperables.spark.wrappers.params.common._
import ai.deepsense.deeplang.params.Param
import ai.deepsense.deeplang.params.choice.Choice
import ai.deepsense.deeplang.params.validators.RangeValidator
import ai.deepsense.deeplang.params.wrappers.spark._

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

  override val params: Array[Param[_]] = Array(maxDepth, maxBins, minInstancesPerNode, minInfoGain, maxMemoryInMB,
    cacheNodeIds, checkpointInterval, impurity, subsamplingRate, seed, numTrees, featureSubsetStrategy, labelColumn,
    featuresColumn, predictionColumn)

}
