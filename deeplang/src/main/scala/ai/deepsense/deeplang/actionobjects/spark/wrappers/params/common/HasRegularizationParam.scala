package ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common

import scala.language.reflectiveCalls

import org.apache.spark.ml

import ai.deepsense.deeplang.parameters.Params
import ai.deepsense.deeplang.parameters.validators.RangeValidator
import ai.deepsense.deeplang.parameters.wrappers.spark.DoubleParameterWrapper

trait HasRegularizationParam extends Params {

  val regularizationParam = new DoubleParameterWrapper[ml.param.Params { val regParam: ml.param.DoubleParam }](
    name = "regularization param",
    description = Some("The regularization parameter."),
    sparkParamGetter = _.regParam,
    validator = RangeValidator(0.0, Double.MaxValue)
  )

  setDefault(regularizationParam, 0.0)

}
