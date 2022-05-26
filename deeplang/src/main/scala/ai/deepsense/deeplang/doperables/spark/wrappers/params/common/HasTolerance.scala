package ai.deepsense.deeplang.doperables.spark.wrappers.params.common

import scala.language.reflectiveCalls

import org.apache.spark.ml

import ai.deepsense.deeplang.params.Params
import ai.deepsense.deeplang.params.validators.RangeValidator
import ai.deepsense.deeplang.params.wrappers.spark.DoubleParamWrapper

trait HasTolerance extends Params {

  lazy val toleranceDefault: Double = 1e-6

  val tolerance = new DoubleParamWrapper[ml.param.Params { val tol: ml.param.DoubleParam }](
    name = "tolerance",
    description = Some("The convergence tolerance for iterative algorithms."),
    sparkParamGetter = _.tol,
    validator = RangeValidator(0.0, 1.0)
  )

  setDefault(tolerance, toleranceDefault)

}
