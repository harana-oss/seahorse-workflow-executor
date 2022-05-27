package ai.deepsense.deeplang.actionobjects.spark.wrappers.models

import org.apache.spark.ml.regression.{DecisionTreeRegressionModel => SparkDecisionTreeRegressionModel}
import org.apache.spark.ml.regression.{DecisionTreeRegressor => SparkDecisionTreeRegressor}

import ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common.HasFeaturesColumnParam
import ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common.HasPredictionColumnCreatorParam
import ai.deepsense.deeplang.actionobjects.LoadableWithFallback
import ai.deepsense.deeplang.actionobjects.SparkModelWrapper
import ai.deepsense.deeplang.parameters.Parameter
import ai.deepsense.sparkutils.ML

class DecisionTreeRegressionModel
    extends SparkModelWrapper[SparkDecisionTreeRegressionModel, SparkDecisionTreeRegressor]
    with HasFeaturesColumnParam
    with HasPredictionColumnCreatorParam
    with LoadableWithFallback[SparkDecisionTreeRegressionModel, SparkDecisionTreeRegressor] {

  override val params: Array[Parameter[_]] = Array(featuresColumn, predictionColumn)

  override def tryToLoadModel(path: String): Option[SparkDecisionTreeRegressionModel] =
    ML.ModelLoading.decisionTreeRegression(path)

}
