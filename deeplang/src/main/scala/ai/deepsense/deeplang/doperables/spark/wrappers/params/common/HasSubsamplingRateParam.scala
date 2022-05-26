package ai.deepsense.deeplang.doperables.spark.wrappers.params.common

import scala.language.reflectiveCalls

import org.apache.spark.ml

import ai.deepsense.deeplang.params.Params
import ai.deepsense.deeplang.params.validators.RangeValidator
import ai.deepsense.deeplang.params.wrappers.spark.DoubleParamWrapper
import ai.deepsense.deeplang.params.wrappers.spark.IntParamWrapper

trait HasSubsamplingRateParam extends Params {

  val subsamplingRate =
    new DoubleParamWrapper[ml.param.Params { val subsamplingRate: ml.param.DoubleParam }](
      name = "subsampling rate",
      description = Some("The fraction of the training data used for learning each decision tree."),
      sparkParamGetter = _.subsamplingRate,
      RangeValidator(0.0, 1.0, beginIncluded = false)
    )

  setDefault(subsamplingRate, 1.0)

}
