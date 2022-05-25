package io.deepsense.deeplang.doperables.spark.wrappers.estimators

import io.deepsense.deeplang.doperables.spark.wrappers.params.common.FeatureSubsetStrategy
import io.deepsense.deeplang.doperables.spark.wrappers.params.common.RegressionImpurity.Variance
import io.deepsense.deeplang.params.ParamPair
import io.deepsense.deeplang.params.selections.NameSingleColumnSelection

class RandomForestRegressionSmokeTest extends AbstractEstimatorModelWrapperSmokeTest {

  override def className: String = "RandomForestRegression"

  override val estimator = new RandomForestRegression()

  import estimator._

  override val estimatorParams: Seq[ParamPair[_]] = Seq(
    maxDepth -> 5.0,
    maxBins -> 32.0,
    minInstancesPerNode -> 1.0,
    minInfoGain -> 0.0,
    maxMemoryInMB -> 256.0,
    cacheNodeIds -> false,
    checkpointInterval -> 10.0,
    impurity -> Variance(),
    subsamplingRate -> 1.0,
    seed -> 1.0,
    numTrees -> 20.0,
    featureSubsetStrategy -> FeatureSubsetStrategy.Auto(),
    featuresColumn -> NameSingleColumnSelection("myFeatures"),
    labelColumn -> NameSingleColumnSelection("myLabel")
  )
}
