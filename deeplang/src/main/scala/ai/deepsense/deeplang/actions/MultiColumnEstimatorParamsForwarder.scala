package ai.deepsense.deeplang.actions

import ai.deepsense.deeplang.actionobjects.MultiColumnEstimator

trait MultiColumnEstimatorParamsForwarder[E <: MultiColumnEstimator[_, _, _]] {
  self: EstimatorAsOperation[E, _] =>

  def setSingleColumn(inputColumnName: String, outputColumnName: String): this.type = {
    estimator.setSingleColumn(inputColumnName, outputColumnName)
    set(estimator.extractParamMap())
    this
  }

  def setMultipleColumn(inputColumnNames: Set[String], outputColumnPrefix: String): this.type = {
    estimator.setMultipleColumn(inputColumnNames, outputColumnPrefix)
    set(estimator.extractParamMap())
    this
  }

}
