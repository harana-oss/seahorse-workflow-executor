package io.deepsense.deeplang.doperables.spark.wrappers.params.common

import scala.language.reflectiveCalls

import org.apache.spark.ml

import io.deepsense.deeplang.params.Params
import io.deepsense.deeplang.params.wrappers.spark.LongParamWrapper

trait HasSeedParam extends Params {

  val seed = new LongParamWrapper[ml.param.Params { val seed: ml.param.LongParam }](
    name = "seed",
    description = Some("The random seed."),
    sparkParamGetter = _.seed)
  setDefault(seed, 0.0)
}
