package ai.deepsense.deeplang.actionobjects.spark.wrappers.estimators

import org.apache.spark.ml.regression.{LinearRegression => SparkLinearRegression}
import org.apache.spark.ml.regression.{LinearRegressionModel => SparkLinearRegressionModel}

import ai.deepsense.deeplang.actionobjects.SparkEstimatorWrapper
import ai.deepsense.deeplang.actionobjects.spark.wrappers.models.LinearRegressionModel
import ai.deepsense.deeplang.actionobjects.spark.wrappers.params.LinearRegressionParams
import ai.deepsense.deeplang.parameters.Parameter

class LinearRegression
    extends SparkEstimatorWrapper[SparkLinearRegressionModel, SparkLinearRegression, LinearRegressionModel]
    with LinearRegressionParams {

  override val params: Array[Parameter[_]] = Array(elasticNetParam, fitIntercept, maxIterations, regularizationParam,
    tolerance, standardization, optionalWeightColumn, solver, labelColumn, featuresColumn, predictionColumn)

}
