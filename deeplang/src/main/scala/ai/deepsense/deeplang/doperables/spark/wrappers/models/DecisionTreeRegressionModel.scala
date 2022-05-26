package ai.deepsense.deeplang.doperables.spark.wrappers.models

import org.apache.spark.ml.regression.{DecisionTreeRegressionModel => SparkDecisionTreeRegressionModel}
import org.apache.spark.ml.regression.{DecisionTreeRegressor => SparkDecisionTreeRegressor}

import ai.deepsense.deeplang.doperables.spark.wrappers.params.common.HasFeaturesColumnParam
import ai.deepsense.deeplang.doperables.spark.wrappers.params.common.HasPredictionColumnCreatorParam
import ai.deepsense.deeplang.doperables.LoadableWithFallback
import ai.deepsense.deeplang.doperables.SparkModelWrapper
import ai.deepsense.deeplang.params.Param
import ai.deepsense.sparkutils.ML

class DecisionTreeRegressionModel
    extends SparkModelWrapper[SparkDecisionTreeRegressionModel, SparkDecisionTreeRegressor]
    with HasFeaturesColumnParam
    with HasPredictionColumnCreatorParam
    with LoadableWithFallback[SparkDecisionTreeRegressionModel, SparkDecisionTreeRegressor] {

  override val params: Array[Param[_]] = Array(featuresColumn, predictionColumn)

  override def tryToLoadModel(path: String): Option[SparkDecisionTreeRegressionModel] =
    ML.ModelLoading.decisionTreeRegression(path)

}
