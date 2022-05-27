package ai.deepsense.deeplang.actionobjects.spark.wrappers.estimators

import scala.language.reflectiveCalls

import org.apache.spark.ml.feature.{StringIndexer => SparkStringIndexer}
import org.apache.spark.ml.feature.{StringIndexerModel => SparkStringIndexerModel}

import ai.deepsense.deeplang.actionobjects.multicolumn.MultiColumnParams.SingleOrMultiColumnChoices.SingleColumnChoice
import ai.deepsense.deeplang.actionobjects.spark.wrappers.models.MultiColumnStringIndexerModel
import ai.deepsense.deeplang.actionobjects.spark.wrappers.models.SingleColumnStringIndexerModel
import ai.deepsense.deeplang.actionobjects.spark.wrappers.models.StringIndexerModel
import ai.deepsense.deeplang.actionobjects.SparkMultiColumnEstimatorWrapper
import ai.deepsense.deeplang.actionobjects.SparkSingleColumnEstimatorWrapper
import ai.deepsense.deeplang.parameters.Parameter

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

  override def getSpecificParams: Array[Parameter[_]] = Array()

}

class SingleStringIndexer
    extends SparkSingleColumnEstimatorWrapper[
      SparkStringIndexerModel,
      SparkStringIndexer,
      SingleColumnStringIndexerModel
    ] {

  override def getSpecificParams: Array[Parameter[_]] = Array()

}
