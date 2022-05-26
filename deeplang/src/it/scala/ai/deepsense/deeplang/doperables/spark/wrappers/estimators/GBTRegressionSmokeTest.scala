package ai.deepsense.deeplang.doperables.spark.wrappers.estimators

import ai.deepsense.deeplang.doperables.spark.wrappers.params.common.RegressionImpurity.Variance
import ai.deepsense.deeplang.params.ParamPair
import ai.deepsense.deeplang.params.selections.NameSingleColumnSelection

class GBTRegressionSmokeTest extends AbstractEstimatorModelWrapperSmokeTest {

  override def className: String = "GBTRegression"

  override val estimator = new GBTRegression()

  private val labelColumnName = "myRating"

  import estimator._

  override val estimatorParams: Seq[ParamPair[_]] = Seq(
    featuresColumn      -> NameSingleColumnSelection("myFeatures"),
    impurity            -> Variance(),
    labelColumn         -> NameSingleColumnSelection(labelColumnName),
    lossType            -> GBTRegression.Squared(),
    maxBins             -> 2.0,
    maxDepth            -> 6.0,
    maxIterations       -> 10.0,
    minInfoGain         -> 0.0,
    minInstancesPerNode -> 1,
    predictionColumn    -> "prediction",
    seed                -> 100.0,
    stepSize            -> 0.11,
    subsamplingRate     -> 0.999
  )

}
