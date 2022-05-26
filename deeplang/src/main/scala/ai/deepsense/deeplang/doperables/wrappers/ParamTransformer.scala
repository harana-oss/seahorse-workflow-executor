package ai.deepsense.deeplang.doperables.wrappers

import org.apache.spark.ml.param.ParamMap

import ai.deepsense.deeplang.params.wrappers.deeplang.ParamWrapper
import ai.deepsense.deeplang.params.Param
import ai.deepsense.deeplang.params.ParamPair

private[wrappers] object ParamTransformer {

  /** Transforms spark ParamMap to a seq of deeplang ParamPair. Transformation is needed when operating on deeplang
    * Estimators, Transformers, Evaluators wrapped to work as Spark entities.
    */
  def transform(sparkParamMap: ParamMap): Seq[ParamPair[_]] = {
    sparkParamMap.toSeq.map { case sparkParamPair =>
      val param: Param[Any] = sparkParamPair.param.asInstanceOf[ParamWrapper[Any]].param
      ParamPair(param, sparkParamPair.value)
    }
  }

}
