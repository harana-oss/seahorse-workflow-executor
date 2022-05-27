package ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common

import scala.language.reflectiveCalls

import ai.deepsense.deeplang.parameters.Params

trait PredictorParams extends Params with HasFeaturesColumnParam with HasPredictionColumnCreatorParam
