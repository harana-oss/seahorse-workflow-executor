package io.deepsense.deeplang.doperables.spark.wrappers.params.common

import scala.language.reflectiveCalls

import org.apache.spark.ml

import io.deepsense.deeplang.params.Params
import io.deepsense.deeplang.params.validators.RangeValidator
import io.deepsense.deeplang.params.wrappers.spark.IntParamWrapper

trait HasNumberOfClustersParam extends Params {

  val k = new IntParamWrapper[ml.param.Params { val k: ml.param.IntParam }](
    name = "k",
    description = Some("The number of clusters to create."),
    sparkParamGetter = _.k,
    validator = RangeValidator(begin = 2.0, end = Int.MaxValue, step = Some(1.0))
  )

  setDefault(k, 2.0)

}
