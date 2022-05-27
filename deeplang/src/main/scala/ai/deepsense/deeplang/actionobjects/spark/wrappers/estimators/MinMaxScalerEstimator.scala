package ai.deepsense.deeplang.actionobjects.spark.wrappers.estimators

import org.apache.spark.ml.feature.{MinMaxScaler => SparkMinMaxScaler}
import org.apache.spark.ml.feature.{MinMaxScalerModel => SparkMinMaxScalerModel}

import ai.deepsense.deeplang.actionobjects.SparkSingleColumnEstimatorWrapper
import ai.deepsense.deeplang.actionobjects.spark.wrappers.models.MinMaxScalerModel
import ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common.HasInputColumn
import ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common.HasOutputColumn
import ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common.MinMaxParams
import ai.deepsense.deeplang.parameters.Parameter

class MinMaxScalerEstimator
    extends SparkSingleColumnEstimatorWrapper[SparkMinMaxScalerModel, SparkMinMaxScaler, MinMaxScalerModel]
    with MinMaxParams
    with HasInputColumn
    with HasOutputColumn {

  override def convertInputNumericToVector: Boolean = true

  override def convertOutputVectorToDouble: Boolean = true

  override def getSpecificParams: Array[Parameter[_]] = Array(min, max)

}
