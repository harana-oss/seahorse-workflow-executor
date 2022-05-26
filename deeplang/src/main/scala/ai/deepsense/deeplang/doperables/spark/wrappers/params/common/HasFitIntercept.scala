package ai.deepsense.deeplang.doperables.spark.wrappers.params.common

import scala.language.reflectiveCalls

import org.apache.spark.ml

import ai.deepsense.deeplang.params.Params
import ai.deepsense.deeplang.params.wrappers.spark.BooleanParamWrapper

trait HasFitIntercept extends Params {

  val fitIntercept = new BooleanParamWrapper[ml.param.Params { val fitIntercept: ml.param.BooleanParam }](
    name = "fit intercept",
    description = Some("Whether to fit an intercept term."),
    sparkParamGetter = _.fitIntercept
  )

  setDefault(fitIntercept, true)

}
