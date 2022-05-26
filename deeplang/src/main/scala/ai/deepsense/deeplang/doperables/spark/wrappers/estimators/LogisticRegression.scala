package ai.deepsense.deeplang.doperables.spark.wrappers.estimators

import org.apache.spark.ml.classification.{LogisticRegression => SparkLogisticRegression}
import org.apache.spark.ml.classification.{LogisticRegressionModel => SparkLogisticRegressionModel}

import ai.deepsense.deeplang.doperables.SparkEstimatorWrapper
import ai.deepsense.deeplang.doperables.spark.wrappers.models.LogisticRegressionModel
import ai.deepsense.deeplang.doperables.spark.wrappers.params.common._
import ai.deepsense.deeplang.params.Param

class LogisticRegression
    extends SparkEstimatorWrapper[SparkLogisticRegressionModel, SparkLogisticRegression, LogisticRegressionModel]
    with ProbabilisticClassifierParams
    with HasLabelColumnParam
    with HasThreshold
    with HasRegularizationParam
    with HasElasticNetParam
    with HasMaxIterationsParam
    with HasTolerance
    with HasFitIntercept
    with HasStandardization
    with HasOptionalWeightColumnParam {

  override lazy val maxIterationsDefault = 100.0

  override val params: Array[Param[_]] = Array(elasticNetParam, fitIntercept, maxIterations, regularizationParam,
    tolerance, standardization, optionalWeightColumn, labelColumn, featuresColumn, probabilityColumn,
    rawPredictionColumn, predictionColumn, threshold)

}
