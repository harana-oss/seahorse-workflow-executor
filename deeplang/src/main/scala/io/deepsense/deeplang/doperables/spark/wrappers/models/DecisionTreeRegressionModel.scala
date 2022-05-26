package io.deepsense.deeplang.doperables.spark.wrappers.models

import org.apache.spark.ml.regression.{DecisionTreeRegressionModel => SparkDecisionTreeRegressionModel}
import org.apache.spark.ml.regression.{DecisionTreeRegressor => SparkDecisionTreeRegressor}

import io.deepsense.deeplang.doperables.spark.wrappers.params.common.HasFeaturesColumnParam
import io.deepsense.deeplang.doperables.spark.wrappers.params.common.HasPredictionColumnCreatorParam
import io.deepsense.deeplang.doperables.LoadableWithFallback
import io.deepsense.deeplang.doperables.SparkModelWrapper
import io.deepsense.deeplang.params.Param
import io.deepsense.sparkutils.ML

class DecisionTreeRegressionModel
    extends SparkModelWrapper[SparkDecisionTreeRegressionModel, SparkDecisionTreeRegressor]
    with HasFeaturesColumnParam
    with HasPredictionColumnCreatorParam
    with LoadableWithFallback[SparkDecisionTreeRegressionModel, SparkDecisionTreeRegressor] {

  override val params: Array[Param[_]] = Array(featuresColumn, predictionColumn)

  override def tryToLoadModel(path: String): Option[SparkDecisionTreeRegressionModel] =
    ML.ModelLoading.decisionTreeRegression(path)

}
