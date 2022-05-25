package io.deepsense.deeplang.doperables.spark.wrappers.params.common

import scala.language.reflectiveCalls

import org.apache.spark.ml

import io.deepsense.deeplang.params.Params
import io.deepsense.deeplang.params.wrappers.spark.DoubleParamWrapper

trait MinMaxParams extends Params {

  val min = new DoubleParamWrapper[ml.param.Params { val min: ml.param.DoubleParam }](
    name = "min",
    description = Some("The lower bound after transformation, shared by all features."),
    sparkParamGetter = _.min)
  setDefault(min, 0.0)

  val max = new DoubleParamWrapper[ml.param.Params { val max: ml.param.DoubleParam }](
    name = "max",
    description = Some("The upper bound after transformation, shared by all features."),
    sparkParamGetter = _.max)
  setDefault(max, 1.0)

  def setMin(value: Double): this.type = {
    set(min, value)
  }

  def setMax(value: Double): this.type = {
    set(max, value)
  }
}
