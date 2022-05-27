package ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common

import scala.language.reflectiveCalls

import org.apache.spark.ml

import ai.deepsense.deeplang.parameters.Params
import ai.deepsense.deeplang.parameters.validators.RangeValidator
import ai.deepsense.deeplang.parameters.wrappers.spark.DoubleParameterWrapper
import ai.deepsense.deeplang.parameters.wrappers.spark.IntParameterWrapper

trait HasSubsamplingRateParam extends Params {

  val subsamplingRate =
    new DoubleParameterWrapper[ml.param.Params { val subsamplingRate: ml.param.DoubleParam }](
      name = "subsampling rate",
      description = Some("The fraction of the training data used for learning each decision tree."),
      sparkParamGetter = _.subsamplingRate,
      RangeValidator(0.0, 1.0, beginIncluded = false)
    )

  setDefault(subsamplingRate, 1.0)

}
