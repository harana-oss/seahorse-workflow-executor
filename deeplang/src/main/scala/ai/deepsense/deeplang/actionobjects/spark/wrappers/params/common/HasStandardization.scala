package ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common

import scala.language.reflectiveCalls

import org.apache.spark.ml

import ai.deepsense.deeplang.parameters.Params
import ai.deepsense.deeplang.parameters.wrappers.spark.BooleanParameterWrapper

trait HasStandardization extends Params {

  val standardization = new BooleanParameterWrapper[ml.param.Params { val standardization: ml.param.BooleanParam }](
    name = "standardization",
    description = Some("Whether to standardize the training features before fitting the model."),
    sparkParamGetter = _.standardization
  )

  setDefault(standardization, true)

}
