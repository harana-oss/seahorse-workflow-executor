package ai.deepsense.deeplang.doperables.spark.wrappers.params.common

import scala.language.reflectiveCalls

import ai.deepsense.deeplang.params.Params

trait PredictorParams extends Params with HasFeaturesColumnParam with HasPredictionColumnCreatorParam
