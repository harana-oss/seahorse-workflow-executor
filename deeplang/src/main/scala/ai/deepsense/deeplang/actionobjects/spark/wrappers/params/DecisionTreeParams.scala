package ai.deepsense.deeplang.actionobjects.spark.wrappers.params

import scala.language.reflectiveCalls

import ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common._
import ai.deepsense.deeplang.parameters.Params

trait DecisionTreeParams
    extends Params
    with PredictorParams
    with HasCheckpointIntervalParam
    with HasSeedParam
    with HasMaxDepthParam
    with HasMaxBinsParam
    with HasMinInstancePerNodeParam
    with HasMinInfoGainParam
    with HasMaxMemoryInMBParam
    with HasCacheNodeIdsParam
