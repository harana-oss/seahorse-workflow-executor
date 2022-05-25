package io.deepsense.deeplang.doperables.spark.wrappers.estimators

import scala.language.reflectiveCalls

import org.apache.spark.ml.feature.{StringIndexer => SparkStringIndexer, StringIndexerModel => SparkStringIndexerModel}

import io.deepsense.deeplang.doperables.multicolumn.MultiColumnParams.SingleOrMultiColumnChoices.SingleColumnChoice
import io.deepsense.deeplang.doperables.spark.wrappers.models.{MultiColumnStringIndexerModel, SingleColumnStringIndexerModel, StringIndexerModel}
import io.deepsense.deeplang.doperables.{SparkMultiColumnEstimatorWrapper, SparkSingleColumnEstimatorWrapper}
import io.deepsense.deeplang.params.Param

class StringIndexerEstimator
  extends SparkMultiColumnEstimatorWrapper[
    SparkStringIndexerModel,
    SparkStringIndexer,
    StringIndexerModel,
    SingleColumnStringIndexerModel,
    SingleStringIndexer,
    MultiColumnStringIndexerModel] {

  setDefault(singleOrMultiChoiceParam, SingleColumnChoice())

  override def getSpecificParams: Array[Param[_]] = Array()
}

class SingleStringIndexer
  extends SparkSingleColumnEstimatorWrapper[
    SparkStringIndexerModel,
    SparkStringIndexer,
    SingleColumnStringIndexerModel] {

  override def getSpecificParams: Array[Param[_]] = Array()
}

