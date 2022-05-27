package ai.deepsense.deeplang.actionobjects.spark.wrappers.estimators

import ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common.OptionalQuantilesColumnChoice
import ai.deepsense.deeplang.parameters.ParamPair
import ai.deepsense.deeplang.parameters.selections.NameSingleColumnSelection

class AFTSurvivalRegressionSmokeTest extends AbstractEstimatorModelWrapperSmokeTest {

  override def className: String = "AFTSurvivalRegression"

  override val estimator = new AFTSurvivalRegression()

  import estimator._

  val optionalQuantilesChoice = OptionalQuantilesColumnChoice.QuantilesColumnNoOption()

  override val estimatorParams: Seq[ParamPair[_]] = Seq(
    censorColumn            -> NameSingleColumnSelection("myCensor"),
    fitIntercept            -> true,
    maxIterations           -> 2.0,
    tolerance               -> 0.01,
    featuresColumn          -> NameSingleColumnSelection("myStandardizedFeatures"),
    labelColumn             -> NameSingleColumnSelection("myNoZeroLabel"),
    predictionColumn        -> "pred",
    optionalQuantilesColumn -> optionalQuantilesChoice,
    quantileProbabilities   -> Array(0.01, 0.05, 0.1, 0.25, 0.5, 0.75, 0.9, 0.95, 0.99)
  )

}
