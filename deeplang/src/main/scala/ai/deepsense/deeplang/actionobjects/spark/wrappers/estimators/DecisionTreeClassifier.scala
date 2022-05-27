package ai.deepsense.deeplang.actionobjects.spark.wrappers.estimators

import org.apache.spark.ml.classification.{DecisionTreeClassificationModel => SparkDecisionTreeClassificationModel}
import org.apache.spark.ml.classification.{DecisionTreeClassifier => SparkDecisionTreeClassifier}

import ai.deepsense.deeplang.actionobjects.SparkEstimatorWrapper
import ai.deepsense.deeplang.actionobjects.spark.wrappers.models.DecisionTreeClassificationModel
import ai.deepsense.deeplang.actionobjects.spark.wrappers.models.VanillaDecisionTreeClassificationModel
import ai.deepsense.deeplang.actionobjects.spark.wrappers.params.DecisionTreeParams
import ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common._
import ai.deepsense.deeplang.actionobjects.stringindexingwrapper.StringIndexingEstimatorWrapper
import ai.deepsense.deeplang.parameters.Parameter

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

  override val params: Array[Parameter[_]] = Array(maxDepth, maxBins, minInstancesPerNode, minInfoGain, maxMemoryInMB,
    cacheNodeIds, checkpointInterval, seed, impurity, labelColumn, featuresColumn, probabilityColumn,
    rawPredictionColumn, predictionColumn)

  override protected def estimatorName: String = classOf[DecisionTreeClassifier].getSimpleName

}
