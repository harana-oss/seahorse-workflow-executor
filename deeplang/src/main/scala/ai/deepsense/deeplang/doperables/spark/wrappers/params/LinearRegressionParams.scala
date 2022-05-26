package ai.deepsense.deeplang.doperables.spark.wrappers.params

import ai.deepsense.deeplang.doperables.spark.wrappers.params.common._
import ai.deepsense.deeplang.params.Params

trait LinearRegressionParams
    extends Params
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
