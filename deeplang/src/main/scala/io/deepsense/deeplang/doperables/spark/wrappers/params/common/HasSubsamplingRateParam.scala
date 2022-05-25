package io.deepsense.deeplang.doperables.spark.wrappers.params.common

import scala.language.reflectiveCalls

import org.apache.spark.ml

import io.deepsense.deeplang.params.Params
import io.deepsense.deeplang.params.validators.RangeValidator
import io.deepsense.deeplang.params.wrappers.spark.{DoubleParamWrapper, IntParamWrapper}

trait HasSubsamplingRateParam extends Params {

  val subsamplingRate =
    new DoubleParamWrapper[ml.param.Params { val subsamplingRate: ml.param.DoubleParam }](
      name = "subsampling rate",
      description =
        Some("The fraction of the training data used for learning each decision tree."),
      sparkParamGetter = _.subsamplingRate,
      RangeValidator(0.0, 1.0, beginIncluded = false))
  setDefault(subsamplingRate, 1.0)

}
