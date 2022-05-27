package ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common

import scala.language.reflectiveCalls

import org.apache.spark.ml

import ai.deepsense.deeplang.parameters.Params
import ai.deepsense.deeplang.parameters.validators.RangeValidator
import ai.deepsense.deeplang.parameters.wrappers.spark.IntParameterWrapper

trait HasMaxIterationsParam extends Params {

  lazy val maxIterationsDefault: Double = 10.0

  val maxIterations = new IntParameterWrapper[ml.param.Params { val maxIter: ml.param.IntParam }](
    name = "max iterations",
    description = Some("The maximum number of iterations."),
    sparkParamGetter = _.maxIter,
    validator = RangeValidator.positiveIntegers
  )

  setDefault(maxIterations, maxIterationsDefault)

}
