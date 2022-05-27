package ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common

import scala.language.reflectiveCalls

import org.apache.spark.ml

import ai.deepsense.deeplang.parameters.Params
import ai.deepsense.deeplang.parameters.validators.RangeValidator
import ai.deepsense.deeplang.parameters.wrappers.spark.IntParameterWrapper

trait HasCheckpointIntervalParam extends Params {

  val checkpointInterval = new IntParameterWrapper[ml.param.Params { val checkpointInterval: ml.param.IntParam }](
    name = "checkpoint interval",
    description = Some("""The checkpoint interval. E.g. 10 means that the cache will get checkpointed
                         |every 10 iterations.""".stripMargin),
    sparkParamGetter = _.checkpointInterval,
    validator = RangeValidator(begin = 1.0, end = Int.MaxValue, step = Some(1.0))
  )

  setDefault(checkpointInterval, 10.0)

}
