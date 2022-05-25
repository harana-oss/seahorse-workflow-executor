package io.deepsense.deeplang.doperables.spark.wrappers.estimators

import io.deepsense.deeplang.doperables.spark.wrappers.params.common.{ClassificationImpurity, FeatureSubsetStrategy}
import io.deepsense.deeplang.params.ParamPair
import io.deepsense.deeplang.params.selections.NameSingleColumnSelection

class RandomForestClassifierSmokeTest
  extends AbstractEstimatorModelWrapperSmokeTest {

  override def className: String = "RandomForestClassifier"

  override val estimator = new RandomForestClassifier()

  import estimator.vanillaRandomForestClassifier._

  override val estimatorParams: Seq[ParamPair[_]] = Seq(
    maxDepth -> 3,
    maxBins -> 40,
    impurity -> ClassificationImpurity.Entropy(),
    featuresColumn -> NameSingleColumnSelection("myFeatures"),
    labelColumn -> NameSingleColumnSelection("myLabel"),
    minInstancesPerNode -> 1,
    minInfoGain -> 2,
    maxMemoryInMB -> 20,
    cacheNodeIds -> true,
    checkpointInterval -> 3,
    subsamplingRate -> 0.5,
    seed -> 555,
    numTrees -> 30,
    featureSubsetStrategy -> FeatureSubsetStrategy.Auto()
  )
}
