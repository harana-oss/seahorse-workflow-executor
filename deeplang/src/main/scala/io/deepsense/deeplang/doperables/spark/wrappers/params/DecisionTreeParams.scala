package io.deepsense.deeplang.doperables.spark.wrappers.params

import scala.language.reflectiveCalls

import io.deepsense.deeplang.doperables.spark.wrappers.params.common._
import io.deepsense.deeplang.params.Params

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
