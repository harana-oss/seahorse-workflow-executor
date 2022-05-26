package io.deepsense.deeplang.doperables.spark.wrappers.estimators

import io.deepsense.deeplang.doperables.spark.wrappers.params.common.RegressionImpurity.Variance
import io.deepsense.deeplang.params.ParamPair
import io.deepsense.deeplang.params.selections.NameSingleColumnSelection

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
