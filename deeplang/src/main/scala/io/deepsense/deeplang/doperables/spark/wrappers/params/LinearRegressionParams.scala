package io.deepsense.deeplang.doperables.spark.wrappers.params

import io.deepsense.deeplang.doperables.spark.wrappers.params.common._
import io.deepsense.deeplang.params.Params

trait LinearRegressionParams extends Params
  with PredictorParams
  with HasLabelColumnParam
  with HasRegularizationParam
  with HasElasticNetParam
  with HasMaxIterationsParam
  with HasTolerance
  with HasFitIntercept
  with HasStandardization
  with HasOptionalWeightColumnParam
  with HasSolverParam
