package io.deepsense.deeplang.doperables.spark.wrappers.estimators

import org.apache.spark.ml.regression.{DecisionTreeRegressionModel => SparkDecisionTreeRegressionModel}
import org.apache.spark.ml.regression.{DecisionTreeRegressor => SparkDecisionTreeRegressor}

import io.deepsense.deeplang.doperables.SparkEstimatorWrapper
import io.deepsense.deeplang.doperables.spark.wrappers.models.DecisionTreeRegressionModel
import io.deepsense.deeplang.doperables.spark.wrappers.params.DecisionTreeParams
import io.deepsense.deeplang.doperables.spark.wrappers.params.common.HasLabelColumnParam
import io.deepsense.deeplang.doperables.spark.wrappers.params.common.HasRegressionImpurityParam
import io.deepsense.deeplang.params.Param

class DecisionTreeRegression
    extends SparkEstimatorWrapper[
      SparkDecisionTreeRegressionModel,
      SparkDecisionTreeRegressor,
      DecisionTreeRegressionModel
    ]
    with DecisionTreeParams
    with HasRegressionImpurityParam
    with HasLabelColumnParam {

  override val params: Array[Param[_]] = Array(
    maxDepth, maxBins, minInstancesPerNode, minInfoGain, maxMemoryInMB, cacheNodeIds, checkpointInterval, seed,
    impurity, labelColumn, featuresColumn, predictionColumn
  )

}
