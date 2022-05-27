package ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common

import scala.language.reflectiveCalls

import org.apache.spark.ml

import ai.deepsense.deeplang.parameters.Params
import ai.deepsense.deeplang.parameters.validators.RangeValidator
import ai.deepsense.deeplang.parameters.wrappers.spark.DoubleParameterWrapper

trait HasStepSizeParam extends Params {

  lazy val stepSizeDefault = 0.1

  val stepSize = new DoubleParameterWrapper[ml.param.Params { val stepSize: ml.param.DoubleParam }](
    name = "step size",
    description = Some("The step size to be used for each iteration of optimization."),
    sparkParamGetter = _.stepSize,
    validator = RangeValidator(begin = 0.0, end = Double.MaxValue)
  )

  setDefault(stepSize, stepSizeDefault)

}
