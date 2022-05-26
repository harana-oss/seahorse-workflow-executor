package io.deepsense.deeplang.doperables.spark.wrappers.estimators

import org.apache.spark.ml.regression.{LinearRegression => SparkLinearRegression}
import org.apache.spark.ml.regression.{LinearRegressionModel => SparkLinearRegressionModel}

import io.deepsense.deeplang.doperables.SparkEstimatorWrapper
import io.deepsense.deeplang.doperables.spark.wrappers.models.LinearRegressionModel
import io.deepsense.deeplang.doperables.spark.wrappers.params.LinearRegressionParams
import io.deepsense.deeplang.params.Param

class LinearRegression
    extends SparkEstimatorWrapper[SparkLinearRegressionModel, SparkLinearRegression, LinearRegressionModel]
    with LinearRegressionParams {

  override val params: Array[Param[_]] = Array(
    elasticNetParam, fitIntercept, maxIterations, regularizationParam, tolerance, standardization, optionalWeightColumn,
    solver, labelColumn, featuresColumn, predictionColumn
  )

}
