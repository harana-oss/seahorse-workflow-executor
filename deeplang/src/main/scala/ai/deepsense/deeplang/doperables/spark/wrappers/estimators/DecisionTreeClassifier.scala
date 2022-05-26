package ai.deepsense.deeplang.doperables.spark.wrappers.estimators

import org.apache.spark.ml.classification.{DecisionTreeClassificationModel => SparkDecisionTreeClassificationModel}
import org.apache.spark.ml.classification.{DecisionTreeClassifier => SparkDecisionTreeClassifier}

import ai.deepsense.deeplang.doperables.SparkEstimatorWrapper
import ai.deepsense.deeplang.doperables.spark.wrappers.models.DecisionTreeClassificationModel
import ai.deepsense.deeplang.doperables.spark.wrappers.models.VanillaDecisionTreeClassificationModel
import ai.deepsense.deeplang.doperables.spark.wrappers.params.DecisionTreeParams
import ai.deepsense.deeplang.doperables.spark.wrappers.params.common._
import ai.deepsense.deeplang.doperables.stringindexingwrapper.StringIndexingEstimatorWrapper
import ai.deepsense.deeplang.params.Param

class DecisionTreeClassifier private (val vanillaDecisionTreeClassifier: VanillaDecisionTreeClassifier)
    extends StringIndexingEstimatorWrapper[
      SparkDecisionTreeClassificationModel,
      SparkDecisionTreeClassifier,
      VanillaDecisionTreeClassificationModel,
      DecisionTreeClassificationModel
    ](vanillaDecisionTreeClassifier) {

  def this() = this(new VanillaDecisionTreeClassifier())

}

class VanillaDecisionTreeClassifier
    extends SparkEstimatorWrapper[
      SparkDecisionTreeClassificationModel,
      SparkDecisionTreeClassifier,
      VanillaDecisionTreeClassificationModel
    ]
    with HasClassificationImpurityParam
    with DecisionTreeParams
    with ProbabilisticClassifierParams
    with HasLabelColumnParam {

  override val params: Array[Param[_]] = Array(maxDepth, maxBins, minInstancesPerNode, minInfoGain, maxMemoryInMB,
    cacheNodeIds, checkpointInterval, seed, impurity, labelColumn, featuresColumn, probabilityColumn,
    rawPredictionColumn, predictionColumn)

  override protected def estimatorName: String = classOf[DecisionTreeClassifier].getSimpleName

}
