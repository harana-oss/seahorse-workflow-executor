package ai.deepsense.deeplang.actionobjects.spark.wrappers.estimators

import ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common.RegressionImpurity.Variance
import ai.deepsense.deeplang.parameters.ParamPair
import ai.deepsense.deeplang.parameters.selections.NameSingleColumnSelection

class DecisionTreeRegressionSmokeTest extends AbstractEstimatorModelWrapperSmokeTest {

  override def className: String = "DecisionTreeRegression"

  override val estimator = new DecisionTreeRegression()

  import estimator._

  override val estimatorParams: Seq[ParamPair[_]] = Seq(
    maxDepth            -> 4.0,
    maxBins             -> 25.0,
    minInstancesPerNode -> 1.0,
    minInfoGain         -> 0.1,
    maxMemoryInMB       -> 200.0,
    cacheNodeIds        -> false,
    checkpointInterval  -> 11.0,
    seed                -> 125.0,
    impurity            -> Variance(),
    featuresColumn      -> NameSingleColumnSelection("myFeatures"),
    labelColumn         -> NameSingleColumnSelection("myLabel"),
    predictionColumn    -> "pred"
  )

}
