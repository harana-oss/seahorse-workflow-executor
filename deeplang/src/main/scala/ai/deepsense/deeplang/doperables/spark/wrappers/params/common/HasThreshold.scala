package ai.deepsense.deeplang.doperables.spark.wrappers.params.common

import ai.deepsense.deeplang.params.Params
import ai.deepsense.deeplang.params.validators.RangeValidator
import ai.deepsense.deeplang.params.wrappers.spark.DoubleParamWrapper
import org.apache.spark.ml

import scala.language.reflectiveCalls

trait HasThreshold extends Params {

  val threshold = new DoubleParamWrapper[ml.param.Params { val threshold: ml.param.DoubleParam }](
    name = "threshold",
    description = Some("The threshold in binary classification prediction."),
    sparkParamGetter = _.threshold,
    validator = RangeValidator(0.0, 1.0)
  )

  setDefault(threshold, 0.5)

}
