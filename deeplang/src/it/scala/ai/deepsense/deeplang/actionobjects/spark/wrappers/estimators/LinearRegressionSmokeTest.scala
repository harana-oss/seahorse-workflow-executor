package ai.deepsense.deeplang.actionobjects.spark.wrappers.estimators

import ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common.SolverChoice
import ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common.OptionalWeightColumnChoice
import ai.deepsense.deeplang.parameters.ParamPair
import ai.deepsense.deeplang.parameters.selections.NameSingleColumnSelection

class LinearRegressionSmokeTest extends AbstractEstimatorModelWrapperSmokeTest {

  override def className: String = "LinearRegression"

  override val estimator = new LinearRegression()

  import estimator._

  val weightColumnChoice = OptionalWeightColumnChoice
    .WeightColumnYesOption()
    .setWeightColumn(NameSingleColumnSelection("myWeight"))

  override val estimatorParams: Seq[ParamPair[_]] = Seq(
    elasticNetParam      -> 0.8,
    fitIntercept         -> true,
    maxIterations        -> 2.0,
    regularizationParam  -> 0.1,
    tolerance            -> 0.01,
    standardization      -> true,
    featuresColumn       -> NameSingleColumnSelection("myFeatures"),
    labelColumn          -> NameSingleColumnSelection("myLabel"),
    predictionColumn     -> "pred",
    optionalWeightColumn -> weightColumnChoice,
    solver               -> SolverChoice.Auto()
  )

}
