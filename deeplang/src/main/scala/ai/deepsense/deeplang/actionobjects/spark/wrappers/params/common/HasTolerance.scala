package ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common

import scala.language.reflectiveCalls

import org.apache.spark.ml

import ai.deepsense.deeplang.parameters.Params
import ai.deepsense.deeplang.parameters.validators.RangeValidator
import ai.deepsense.deeplang.parameters.wrappers.spark.DoubleParameterWrapper

trait HasTolerance extends Params {

  lazy val toleranceDefault: Double = 1e-6

  val tolerance = new DoubleParameterWrapper[ml.param.Params { val tol: ml.param.DoubleParam }](
    name = "tolerance",
    description = Some("The convergence tolerance for iterative algorithms."),
    sparkParamGetter = _.tol,
    validator = RangeValidator(0.0, 1.0)
  )

  setDefault(tolerance, toleranceDefault)

}
