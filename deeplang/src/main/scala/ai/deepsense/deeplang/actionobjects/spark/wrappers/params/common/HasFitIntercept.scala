package ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common

import scala.language.reflectiveCalls

import org.apache.spark.ml

import ai.deepsense.deeplang.parameters.Params
import ai.deepsense.deeplang.parameters.wrappers.spark.BooleanParameterWrapper

trait HasFitIntercept extends Params {

  val fitIntercept = new BooleanParameterWrapper[ml.param.Params { val fitIntercept: ml.param.BooleanParam }](
    name = "fit intercept",
    description = Some("Whether to fit an intercept term."),
    sparkParamGetter = _.fitIntercept
  )

  setDefault(fitIntercept, true)

}
