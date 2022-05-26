package ai.deepsense.deeplang.doperables.spark.wrappers.estimators

import scala.language.reflectiveCalls

import org.apache.spark.ml.feature.{StringIndexer => SparkStringIndexer}
import org.apache.spark.ml.feature.{StringIndexerModel => SparkStringIndexerModel}

import ai.deepsense.deeplang.doperables.multicolumn.MultiColumnParams.SingleOrMultiColumnChoices.SingleColumnChoice
import ai.deepsense.deeplang.doperables.spark.wrappers.models.MultiColumnStringIndexerModel
import ai.deepsense.deeplang.doperables.spark.wrappers.models.SingleColumnStringIndexerModel
import ai.deepsense.deeplang.doperables.spark.wrappers.models.StringIndexerModel
import ai.deepsense.deeplang.doperables.SparkMultiColumnEstimatorWrapper
import ai.deepsense.deeplang.doperables.SparkSingleColumnEstimatorWrapper
import ai.deepsense.deeplang.params.Param

class StringIndexerEstimator
    extends SparkMultiColumnEstimatorWrapper[
      SparkStringIndexerModel,
      SparkStringIndexer,
      StringIndexerModel,
      SingleColumnStringIndexerModel,
      SingleStringIndexer,
      MultiColumnStringIndexerModel
    ] {

  setDefault(singleOrMultiChoiceParam, SingleColumnChoice())

  override def getSpecificParams: Array[Param[_]] = Array()

}

class SingleStringIndexer
    extends SparkSingleColumnEstimatorWrapper[
      SparkStringIndexerModel,
      SparkStringIndexer,
      SingleColumnStringIndexerModel
    ] {

  override def getSpecificParams: Array[Param[_]] = Array()

}
