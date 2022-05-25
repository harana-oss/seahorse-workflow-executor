package io.deepsense.deeplang.doperables.spark.wrappers.estimators

import scala.language.reflectiveCalls

import org.apache.spark.ml
import org.apache.spark.ml.feature.{ChiSqSelector => SparkChiSqSelector, ChiSqSelectorModel => SparkChiSqSelectorModel}

import io.deepsense.deeplang.doperables.SparkEstimatorWrapper
import io.deepsense.deeplang.doperables.spark.wrappers.models.ChiSqSelectorModel
import io.deepsense.deeplang.doperables.spark.wrappers.params.common.{HasFeaturesColumnParam, HasLabelColumnParam, HasOutputColumn}
import io.deepsense.deeplang.params.Param
import io.deepsense.deeplang.params.validators.RangeValidator
import io.deepsense.deeplang.params.wrappers.spark.IntParamWrapper

class ChiSqSelectorEstimator
  extends SparkEstimatorWrapper[
    SparkChiSqSelectorModel,
    SparkChiSqSelector,
    ChiSqSelectorModel]
  with HasFeaturesColumnParam
  with HasOutputColumn
  with HasLabelColumnParam{

  val numTopFeatures = new IntParamWrapper[
    ml.param.Params { val numTopFeatures: ml.param.IntParam }](
    name = "num top features",
    description = Some("Number of features that selector will select, ordered by statistics value " +
      "descending. If the real number of features is lower, then this will select all " +
      "features."),
    sparkParamGetter = _.numTopFeatures,
    validator = RangeValidator(begin = 1.0, end = Int.MaxValue, step = Some(1.0)))
  setDefault(numTopFeatures -> 50)

  override val params: Array[Param[_]] = Array(
    numTopFeatures,
    featuresColumn,
    outputColumn,
    labelColumn)

  def setNumTopFeatures(value: Int): this.type = set(numTopFeatures -> value)
}
