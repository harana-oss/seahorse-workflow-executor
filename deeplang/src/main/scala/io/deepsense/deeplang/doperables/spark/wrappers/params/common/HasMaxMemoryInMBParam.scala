package io.deepsense.deeplang.doperables.spark.wrappers.params.common

import scala.language.reflectiveCalls

import org.apache.spark.ml
import org.apache.spark.ml.regression.RandomForestRegressor

import io.deepsense.deeplang.params.Params
import io.deepsense.deeplang.params.validators.RangeValidator
import io.deepsense.deeplang.params.wrappers.spark.IntParamWrapper
import io.deepsense.deeplang.params.wrappers.spark.DoubleParamWrapper

trait HasMaxMemoryInMBParam extends Params {

  val maxMemoryInMB = new IntParamWrapper[ml.param.Params { val maxMemoryInMB: ml.param.IntParam }](
    name = "max memory",
    description = Some("Maximum memory in MB allocated to histogram aggregation."),
    sparkParamGetter = _.maxMemoryInMB,
    validator = RangeValidator.positiveIntegers
  )

  setDefault(maxMemoryInMB, 256.0)

}
