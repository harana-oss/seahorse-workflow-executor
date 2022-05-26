package io.deepsense.deeplang.doperables.spark.wrappers.params.common

import scala.language.reflectiveCalls

import org.apache.spark.ml

import io.deepsense.deeplang.params.Params
import io.deepsense.deeplang.params.validators.RangeValidator
import io.deepsense.deeplang.params.wrappers.spark.DoubleParamWrapper

trait HasRegularizationParam extends Params {

  val regularizationParam = new DoubleParamWrapper[ml.param.Params { val regParam: ml.param.DoubleParam }](
    name = "regularization param",
    description = Some("The regularization parameter."),
    sparkParamGetter = _.regParam,
    validator = RangeValidator(0.0, Double.MaxValue)
  )

  setDefault(regularizationParam, 0.0)

}
