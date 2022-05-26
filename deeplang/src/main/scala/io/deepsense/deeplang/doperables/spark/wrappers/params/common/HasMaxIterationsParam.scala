package io.deepsense.deeplang.doperables.spark.wrappers.params.common

import scala.language.reflectiveCalls

import org.apache.spark.ml

import io.deepsense.deeplang.params.Params
import io.deepsense.deeplang.params.validators.RangeValidator
import io.deepsense.deeplang.params.wrappers.spark.IntParamWrapper

trait HasMaxIterationsParam extends Params {

  lazy val maxIterationsDefault: Double = 10.0

  val maxIterations = new IntParamWrapper[ml.param.Params { val maxIter: ml.param.IntParam }](
    name = "max iterations",
    description = Some("The maximum number of iterations."),
    sparkParamGetter = _.maxIter,
    validator = RangeValidator.positiveIntegers
  )

  setDefault(maxIterations, maxIterationsDefault)

}
