package io.deepsense.deeplang.doperables.spark.wrappers.params.common

import scala.language.reflectiveCalls

import org.apache.spark.ml
import org.apache.spark.ml.regression.RandomForestRegressor

import io.deepsense.deeplang.params.Params
import io.deepsense.deeplang.params.validators.RangeValidator
import io.deepsense.deeplang.params.wrappers.spark.{DoubleParamWrapper, IntParamWrapper}

trait HasMinInfoGainParam extends Params {

  val minInfoGain =
    new DoubleParamWrapper[ml.param.Params { val minInfoGain: ml.param.DoubleParam }](
      name = "min information gain",
      description = Some("The minimum information gain for a split to be considered at a tree node."),
      sparkParamGetter = _.minInfoGain,
      RangeValidator(0.0, Double.MaxValue))
  setDefault(minInfoGain, 0.0)

}
