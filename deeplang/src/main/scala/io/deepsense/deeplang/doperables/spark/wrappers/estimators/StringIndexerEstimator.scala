package io.deepsense.deeplang.doperables.spark.wrappers.estimators

import scala.language.reflectiveCalls

import org.apache.spark.ml.feature.{StringIndexer => SparkStringIndexer}
import org.apache.spark.ml.feature.{StringIndexerModel => SparkStringIndexerModel}

import io.deepsense.deeplang.doperables.multicolumn.MultiColumnParams.SingleOrMultiColumnChoices.SingleColumnChoice
import io.deepsense.deeplang.doperables.spark.wrappers.models.MultiColumnStringIndexerModel
import io.deepsense.deeplang.doperables.spark.wrappers.models.SingleColumnStringIndexerModel
import io.deepsense.deeplang.doperables.spark.wrappers.models.StringIndexerModel
import io.deepsense.deeplang.doperables.SparkMultiColumnEstimatorWrapper
import io.deepsense.deeplang.doperables.SparkSingleColumnEstimatorWrapper
import io.deepsense.deeplang.params.Param

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
