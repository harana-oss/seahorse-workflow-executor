package ai.deepsense.deeplang.actionobjects.spark.wrappers.estimators

import org.apache.spark.ml.classification.{RandomForestClassificationModel => SparkRandomForestClassificationModel}
import org.apache.spark.ml.classification.{RandomForestClassifier => SparkRandomForestClassifier}

import ai.deepsense.deeplang.actionobjects.SparkEstimatorWrapper
import ai.deepsense.deeplang.actionobjects.spark.wrappers.models.RandomForestClassificationModel
import ai.deepsense.deeplang.actionobjects.spark.wrappers.models.VanillaRandomForestClassificationModel
import ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common._
import ai.deepsense.deeplang.actionobjects.stringindexingwrapper.StringIndexingEstimatorWrapper
import ai.deepsense.deeplang.parameters.Parameter

class RandomForestClassifier private (val vanillaRandomForestClassifier: VanillaRandomForestClassifier)
    extends StringIndexingEstimatorWrapper[
      SparkRandomForestClassificationModel,
      SparkRandomForestClassifier,
      VanillaRandomForestClassificationModel,
      RandomForestClassificationModel
    ](vanillaRandomForestClassifier) {

  def this() = this(new VanillaRandomForestClassifier())

}

class VanillaRandomForestClassifier
    extends SparkEstimatorWrapper[
      SparkRandomForestClassificationModel,
      SparkRandomForestClassifier,
      VanillaRandomForestClassificationModel
    ]
    with HasMaxDepthParam
    with HasMaxBinsParam
    with HasMinInstancePerNodeParam
    with HasMinInfoGainParam
    with HasMaxMemoryInMBParam
    with HasCacheNodeIdsParam
    with HasCheckpointIntervalParam
    with HasSubsamplingRateParam
    with HasSeedParam
    with HasNumTreesParam
    with HasFeatureSubsetStrategyParam
    with PredictorParams
    with HasLabelColumnParam
    with ProbabilisticClassifierParams
    with HasClassificationImpurityParam {

  override val params: Array[Parameter[_]] = Array(
    maxDepth, maxBins, minInstancesPerNode, minInfoGain, maxMemoryInMB, cacheNodeIds, checkpointInterval, impurity,
    subsamplingRate, seed, numTrees, featureSubsetStrategy, labelColumn, featuresColumn, probabilityColumn,
    rawPredictionColumn, predictionColumn
    // TODO Thresholds param
  )

  override protected def estimatorName: String = classOf[RandomForestClassifier].getSimpleName

}
