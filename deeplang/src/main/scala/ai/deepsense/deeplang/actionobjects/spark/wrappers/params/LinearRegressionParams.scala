package ai.deepsense.deeplang.actionobjects.spark.wrappers.params

import ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common._
import ai.deepsense.deeplang.parameters.Params

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
