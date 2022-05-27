package ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common

import scala.language.reflectiveCalls

import org.apache.spark.ml

import ai.deepsense.deeplang.parameters.Params
import ai.deepsense.deeplang.parameters.wrappers.spark.DoubleParameterWrapper

trait MinMaxParams extends Params {

  val min = new DoubleParameterWrapper[ml.param.Params { val min: ml.param.DoubleParam }](
    name = "min",
    description = Some("The lower bound after transformation, shared by all features."),
    sparkParamGetter = _.min
  )

  setDefault(min, 0.0)

  val max = new DoubleParameterWrapper[ml.param.Params { val max: ml.param.DoubleParam }](
    name = "max",
    description = Some("The upper bound after transformation, shared by all features."),
    sparkParamGetter = _.max
  )

  setDefault(max, 1.0)

  def setMin(value: Double): this.type =
    set(min, value)

  def setMax(value: Double): this.type =
    set(max, value)

}
