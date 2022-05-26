package io.deepsense.deeplang.doperables.spark.wrappers.estimators

import org.apache.spark.ml.feature.{StandardScaler => SparkStandardScaler}
import org.apache.spark.ml.feature.{StandardScalerModel => SparkStandardScalerModel}

import io.deepsense.deeplang.doperables.SparkSingleColumnEstimatorWrapper
import io.deepsense.deeplang.doperables.spark.wrappers.models.StandardScalerModel
import io.deepsense.deeplang.params.Param
import io.deepsense.deeplang.params.wrappers.spark.BooleanParamWrapper

class StandardScalerEstimator
    extends SparkSingleColumnEstimatorWrapper[SparkStandardScalerModel, SparkStandardScaler, StandardScalerModel] {

  override def convertInputNumericToVector: Boolean = true

  override def convertOutputVectorToDouble: Boolean = true

  val withMean = new BooleanParamWrapper[SparkStandardScaler](
    name = "with mean",
    description = Some("Whether to center data with mean."),
    sparkParamGetter = _.withMean
  )

  setDefault(withMean, false)

  val withStd = new BooleanParamWrapper[SparkStandardScaler](
    name = "with std",
    description = Some("Whether to scale the data to unit standard deviation."),
    sparkParamGetter = _.withStd
  )

  setDefault(withStd, true)

  override protected def getSpecificParams: Array[Param[_]] = Array(withMean, withStd)

}
