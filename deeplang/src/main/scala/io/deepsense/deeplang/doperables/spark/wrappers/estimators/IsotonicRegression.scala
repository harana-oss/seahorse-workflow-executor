package io.deepsense.deeplang.doperables.spark.wrappers.estimators

import org.apache.spark.ml.regression.{IsotonicRegression => SparkIsotonicRegression}
import org.apache.spark.ml.regression.{IsotonicRegressionModel => SparkIsotonicRegressionModel}

import io.deepsense.deeplang.doperables.SparkEstimatorWrapper
import io.deepsense.deeplang.doperables.spark.wrappers.models.IsotonicRegressionModel
import io.deepsense.deeplang.doperables.spark.wrappers.params.common._
import io.deepsense.deeplang.params.wrappers.spark.BooleanParamWrapper

class IsotonicRegression
    extends SparkEstimatorWrapper[SparkIsotonicRegressionModel, SparkIsotonicRegression, IsotonicRegressionModel]
    with PredictorParams
    with HasFeatureIndexParam
    with HasLabelColumnParam
    with HasOptionalWeightColumnParam {

  val isotonic = new BooleanParamWrapper[SparkIsotonicRegression](
    name = "isotonic",
    description = Some("""Whether the output sequence should be isotonic/increasing (true)
                         |or antitonic/decreasing (false).""".stripMargin),
    sparkParamGetter = _.isotonic
  )

  setDefault(isotonic, true)

  override val params: Array[io.deepsense.deeplang.params.Param[_]] =
    Array(isotonic, optionalWeightColumn, featureIndex, labelColumn, featuresColumn, predictionColumn)

}
