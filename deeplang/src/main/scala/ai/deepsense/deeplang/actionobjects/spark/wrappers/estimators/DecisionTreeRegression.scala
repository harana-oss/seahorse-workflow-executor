package ai.deepsense.deeplang.actionobjects.spark.wrappers.estimators

import org.apache.spark.ml.regression.{DecisionTreeRegressionModel => SparkDecisionTreeRegressionModel}
import org.apache.spark.ml.regression.{DecisionTreeRegressor => SparkDecisionTreeRegressor}

import ai.deepsense.deeplang.actionobjects.SparkEstimatorWrapper
import ai.deepsense.deeplang.actionobjects.spark.wrappers.models.DecisionTreeRegressionModel
import ai.deepsense.deeplang.actionobjects.spark.wrappers.params.DecisionTreeParams
import ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common.HasLabelColumnParam
import ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common.HasRegressionImpurityParam
import ai.deepsense.deeplang.parameters.Parameter

class DecisionTreeRegression
    extends SparkEstimatorWrapper[
      SparkDecisionTreeRegressionModel,
      SparkDecisionTreeRegressor,
      DecisionTreeRegressionModel
    ]
    with DecisionTreeParams
    with HasRegressionImpurityParam
    with HasLabelColumnParam {

  override val params: Array[Parameter[_]] = Array(maxDepth, maxBins, minInstancesPerNode, minInfoGain, maxMemoryInMB,
    cacheNodeIds, checkpointInterval, seed, impurity, labelColumn, featuresColumn, predictionColumn)

}
