package io.deepsense.deeplang.doperables.spark.wrappers.params.common

import scala.language.reflectiveCalls

import org.apache.spark.ml

import io.deepsense.deeplang.params.Params
import io.deepsense.deeplang.params.wrappers.spark.BooleanParamWrapper

trait HasStandardization extends Params {

  val standardization = new BooleanParamWrapper[ml.param.Params { val standardization: ml.param.BooleanParam }](
    name = "standardization",
    description = Some("Whether to standardize the training features before fitting the model."),
    sparkParamGetter = _.standardization
  )

  setDefault(standardization, true)

}
