package ai.deepsense.deeplang.actionobjects.spark.wrappers.estimators

import org.apache.spark.ml.feature.{StandardScaler => SparkStandardScaler}
import org.apache.spark.ml.feature.{StandardScalerModel => SparkStandardScalerModel}

import ai.deepsense.deeplang.actionobjects.SparkSingleColumnEstimatorWrapper
import ai.deepsense.deeplang.actionobjects.spark.wrappers.models.StandardScalerModel
import ai.deepsense.deeplang.parameters.Parameter
import ai.deepsense.deeplang.parameters.wrappers.spark.BooleanParameterWrapper

class StandardScalerEstimator
    extends SparkSingleColumnEstimatorWrapper[SparkStandardScalerModel, SparkStandardScaler, StandardScalerModel] {

  override def convertInputNumericToVector: Boolean = true

  override def convertOutputVectorToDouble: Boolean = true

  val withMean = new BooleanParameterWrapper[SparkStandardScaler](
    name = "with mean",
    description = Some("Whether to center data with mean."),
    sparkParamGetter = _.withMean
  )

  setDefault(withMean, false)

  val withStd = new BooleanParameterWrapper[SparkStandardScaler](
    name = "with std",
    description = Some("Whether to scale the data to unit standard deviation."),
    sparkParamGetter = _.withStd
  )

  setDefault(withStd, true)

  override protected def getSpecificParams: Array[Parameter[_]] = Array(withMean, withStd)

}
