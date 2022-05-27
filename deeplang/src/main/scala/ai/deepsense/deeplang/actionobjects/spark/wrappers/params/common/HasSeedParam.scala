package ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common

import scala.language.reflectiveCalls

import org.apache.spark.ml

import ai.deepsense.deeplang.parameters.Params
import ai.deepsense.deeplang.parameters.wrappers.spark.LongParameterWrapper

trait HasSeedParam extends Params {

  val seed = new LongParameterWrapper[ml.param.Params { val seed: ml.param.LongParam }](
    name = "seed",
    description = Some("The random seed."),
    sparkParamGetter = _.seed
  )

  setDefault(seed, 0.0)

}
