package ai.deepsense.deeplang.actionobjects.spark.wrappers.params

import scala.language.reflectiveCalls

import org.apache.spark.ml

import ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common._
import ai.deepsense.deeplang.parameters.Params
import ai.deepsense.deeplang.parameters.validators.RangeValidator
import ai.deepsense.deeplang.parameters.wrappers.spark.DoubleParameterWrapper
import ai.deepsense.deeplang.parameters.wrappers.spark.IntParameterWrapper

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
