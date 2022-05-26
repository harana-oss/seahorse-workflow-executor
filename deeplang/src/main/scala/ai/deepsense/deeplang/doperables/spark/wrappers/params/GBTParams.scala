package ai.deepsense.deeplang.doperables.spark.wrappers.params

import scala.language.reflectiveCalls

import org.apache.spark.ml

import ai.deepsense.deeplang.doperables.spark.wrappers.params.common._
import ai.deepsense.deeplang.params.Params
import ai.deepsense.deeplang.params.validators.RangeValidator
import ai.deepsense.deeplang.params.wrappers.spark.DoubleParamWrapper
import ai.deepsense.deeplang.params.wrappers.spark.IntParamWrapper

trait GBTParams
    extends Params
    with PredictorParams
    with HasLabelColumnParam
    with HasMaxIterationsParam
    with HasSeedParam
    with HasStepSizeParam
    with HasMaxBinsParam
    with HasMaxDepthParam
    with HasMinInfoGainParam
    with HasMinInstancePerNodeParam
    with HasSubsamplingRateParam
