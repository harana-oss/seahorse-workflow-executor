package io.deepsense.deeplang.doperables.spark.wrappers.estimators

import scala.language.reflectiveCalls

import org.apache.spark.ml
import org.apache.spark.ml.feature.{Bucketizer => SparkQuantileDiscretizerModel}
import org.apache.spark.ml.feature.{QuantileDiscretizer => SparkQuantileDiscretizer}

import io.deepsense.deeplang.doperables.SparkSingleColumnEstimatorWrapper
import io.deepsense.deeplang.doperables.spark.wrappers.models.QuantileDiscretizerModel
import io.deepsense.deeplang.params.Param
import io.deepsense.deeplang.params.validators.RangeValidator
import io.deepsense.deeplang.params.wrappers.spark.IntParamWrapper

class QuantileDiscretizerEstimator
    extends SparkSingleColumnEstimatorWrapper[
      SparkQuantileDiscretizerModel,
      SparkQuantileDiscretizer,
      QuantileDiscretizerModel
    ] {

  val numBuckets = new IntParamWrapper[ml.param.Params { val numBuckets: ml.param.IntParam }](
    name = "num buckets",
    description = Some(
      "Maximum number of buckets (quantiles or categories) " +
        "into which the data points are grouped. Must be >= 2."
    ),
    sparkParamGetter = _.numBuckets,
    RangeValidator(2.0, Int.MaxValue, step = Some(1.0))
  )

  setDefault(numBuckets, 2.0)

  override protected def getSpecificParams: Array[Param[_]] = Array(numBuckets)

  def setNumBuckets(value: Int): this.type = set(numBuckets -> value)

}
