package io.deepsense.deeplang.doperables.spark.wrappers.params.common

import scala.language.reflectiveCalls

import org.apache.spark.ml

import io.deepsense.deeplang.params.Params
import io.deepsense.deeplang.params.validators.RangeValidator
import io.deepsense.deeplang.params.wrappers.spark.IntParamWrapper

trait HasCheckpointIntervalParam extends Params {

  val checkpointInterval = new IntParamWrapper[ml.param.Params { val checkpointInterval: ml.param.IntParam }](
    name = "checkpoint interval",
    description = Some("""The checkpoint interval. E.g. 10 means that the cache will get checkpointed
                         |every 10 iterations.""".stripMargin),
    sparkParamGetter = _.checkpointInterval,
    validator = RangeValidator(begin = 1.0, end = Int.MaxValue, step = Some(1.0))
  )

  setDefault(checkpointInterval, 10.0)

}
