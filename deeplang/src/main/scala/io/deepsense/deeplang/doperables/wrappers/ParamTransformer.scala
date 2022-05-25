package io.deepsense.deeplang.doperables.wrappers

import org.apache.spark.ml.param.ParamMap

import io.deepsense.deeplang.params.wrappers.deeplang.ParamWrapper
import io.deepsense.deeplang.params.{Param, ParamPair}

private[wrappers] object ParamTransformer {

  /**
    * Transforms spark ParamMap to a seq of deeplang ParamPair.
    * Transformation is needed when operating on deeplang Estimators, Transformers, Evaluators
    * wrapped to work as Spark entities.
    */
  def transform(sparkParamMap: ParamMap): Seq[ParamPair[_]] = {
    sparkParamMap.toSeq.map{ case sparkParamPair =>
      val param: Param[Any] = sparkParamPair.param.asInstanceOf[ParamWrapper[Any]].param
      ParamPair(param, sparkParamPair.value)
    }
  }
}
