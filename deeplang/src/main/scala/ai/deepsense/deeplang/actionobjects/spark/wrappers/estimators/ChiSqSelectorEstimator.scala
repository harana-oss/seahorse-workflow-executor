package ai.deepsense.deeplang.actionobjects.spark.wrappers.estimators

import scala.language.reflectiveCalls

import org.apache.spark.ml
import org.apache.spark.ml.feature.{ChiSqSelector => SparkChiSqSelector}
import org.apache.spark.ml.feature.{ChiSqSelectorModel => SparkChiSqSelectorModel}

import ai.deepsense.deeplang.actionobjects.SparkEstimatorWrapper
import ai.deepsense.deeplang.actionobjects.spark.wrappers.models.ChiSqSelectorModel
import ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common.HasFeaturesColumnParam
import ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common.HasLabelColumnParam
import ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common.HasOutputColumn
import ai.deepsense.deeplang.parameters.Parameter
import ai.deepsense.deeplang.parameters.validators.RangeValidator
import ai.deepsense.deeplang.parameters.wrappers.spark.IntParameterWrapper

class ChiSqSelectorEstimator
    extends SparkEstimatorWrapper[SparkChiSqSelectorModel, SparkChiSqSelector, ChiSqSelectorModel]
    with HasFeaturesColumnParam
    with HasOutputColumn
    with HasLabelColumnParam {

  val numTopFeatures = new IntParameterWrapper[ml.param.Params { val numTopFeatures: ml.param.IntParam }](
    name = "num top features",
    description = Some(
      "Number of features that selector will select, ordered by statistics value " +
        "descending. If the real number of features is lower, then this will select all " +
        "features."
    ),
    sparkParamGetter = _.numTopFeatures,
    validator = RangeValidator(begin = 1.0, end = Int.MaxValue, step = Some(1.0))
  )

  setDefault(numTopFeatures -> 50)

  override val params: Array[Parameter[_]] = Array(numTopFeatures, featuresColumn, outputColumn, labelColumn)

  def setNumTopFeatures(value: Int): this.type = set(numTopFeatures -> value)

}
