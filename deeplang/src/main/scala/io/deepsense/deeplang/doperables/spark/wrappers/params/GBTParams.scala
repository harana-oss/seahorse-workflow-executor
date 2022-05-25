package io.deepsense.deeplang.doperables.spark.wrappers.params

import scala.language.reflectiveCalls

import org.apache.spark.ml

import io.deepsense.deeplang.doperables.spark.wrappers.params.common._
import io.deepsense.deeplang.params.Params
import io.deepsense.deeplang.params.validators.RangeValidator
import io.deepsense.deeplang.params.wrappers.spark.{DoubleParamWrapper, IntParamWrapper}

trait GBTParams extends Params
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
