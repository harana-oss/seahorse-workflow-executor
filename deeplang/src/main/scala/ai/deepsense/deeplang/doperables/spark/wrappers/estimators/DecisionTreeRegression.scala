package ai.deepsense.deeplang.doperables.spark.wrappers.estimators

import org.apache.spark.ml.regression.{DecisionTreeRegressionModel => SparkDecisionTreeRegressionModel}
import org.apache.spark.ml.regression.{DecisionTreeRegressor => SparkDecisionTreeRegressor}

import ai.deepsense.deeplang.doperables.SparkEstimatorWrapper
import ai.deepsense.deeplang.doperables.spark.wrappers.models.DecisionTreeRegressionModel
import ai.deepsense.deeplang.doperables.spark.wrappers.params.DecisionTreeParams
import ai.deepsense.deeplang.doperables.spark.wrappers.params.common.HasLabelColumnParam
import ai.deepsense.deeplang.doperables.spark.wrappers.params.common.HasRegressionImpurityParam
import ai.deepsense.deeplang.params.Param

class DecisionTreeRegression
    extends SparkEstimatorWrapper[
      SparkDecisionTreeRegressionModel,
      SparkDecisionTreeRegressor,
      DecisionTreeRegressionModel
    ]
    with DecisionTreeParams
    with HasRegressionImpurityParam
    with HasLabelColumnParam {

  override val params: Array[Param[_]] = Array(maxDepth, maxBins, minInstancesPerNode, minInfoGain, maxMemoryInMB,
    cacheNodeIds, checkpointInterval, seed, impurity, labelColumn, featuresColumn, predictionColumn)

}
