package ai.deepsense.deeplang.doperables.spark.wrappers.estimators

import org.apache.spark.ml.feature.{MinMaxScaler => SparkMinMaxScaler}
import org.apache.spark.ml.feature.{MinMaxScalerModel => SparkMinMaxScalerModel}

import ai.deepsense.deeplang.doperables.SparkSingleColumnEstimatorWrapper
import ai.deepsense.deeplang.doperables.spark.wrappers.models.MinMaxScalerModel
import ai.deepsense.deeplang.doperables.spark.wrappers.params.common.HasInputColumn
import ai.deepsense.deeplang.doperables.spark.wrappers.params.common.HasOutputColumn
import ai.deepsense.deeplang.doperables.spark.wrappers.params.common.MinMaxParams
import ai.deepsense.deeplang.params.Param

class MinMaxScalerEstimator
    extends SparkSingleColumnEstimatorWrapper[SparkMinMaxScalerModel, SparkMinMaxScaler, MinMaxScalerModel]
    with MinMaxParams
    with HasInputColumn
    with HasOutputColumn {

  override def convertInputNumericToVector: Boolean = true

  override def convertOutputVectorToDouble: Boolean = true

  override def getSpecificParams: Array[Param[_]] = Array(min, max)

}
