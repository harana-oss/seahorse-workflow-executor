package ai.deepsense.deeplang.actionobjects.spark.wrappers.estimators

import scala.language.reflectiveCalls

import org.apache.spark.ml
import org.apache.spark.ml.feature.{Bucketizer => SparkQuantileDiscretizerModel}
import org.apache.spark.ml.feature.{QuantileDiscretizer => SparkQuantileDiscretizer}

import ai.deepsense.deeplang.actionobjects.SparkSingleColumnEstimatorWrapper
import ai.deepsense.deeplang.actionobjects.spark.wrappers.models.QuantileDiscretizerModel
import ai.deepsense.deeplang.parameters.Parameter
import ai.deepsense.deeplang.parameters.validators.RangeValidator
import ai.deepsense.deeplang.parameters.wrappers.spark.IntParameterWrapper

class QuantileDiscretizerEstimator
    extends SparkSingleColumnEstimatorWrapper[
      SparkQuantileDiscretizerModel,
      SparkQuantileDiscretizer,
      QuantileDiscretizerModel
    ] {

  val numBuckets = new IntParameterWrapper[ml.param.Params { val numBuckets: ml.param.IntParam }](
    name = "num buckets",
    description = Some(
      "Maximum number of buckets (quantiles or categories) " +
        "into which the data points are grouped. Must be >= 2."
    ),
    sparkParamGetter = _.numBuckets,
    RangeValidator(2.0, Int.MaxValue, step = Some(1.0))
  )

  setDefault(numBuckets, 2.0)

  override protected def getSpecificParams: Array[Parameter[_]] = Array(numBuckets)

  def setNumBuckets(value: Int): this.type = set(numBuckets -> value)

}
