package ai.deepsense.deeplang.actionobjects.spark.wrappers.estimators

import ai.deepsense.deeplang.parameters.ParamPair
import ai.deepsense.deeplang.parameters.selections.NameSingleColumnSelection

class KMeansSmokeTest extends AbstractEstimatorModelWrapperSmokeTest {

  override def className: String = "KMeans"

  override val estimator = new KMeans()

  import estimator._

  override val estimatorParams: Seq[ParamPair[_]] = Seq(
    featuresColumn   -> NameSingleColumnSelection("myFeatures"),
    k                -> 3.0,
    maxIterations    -> 20.0,
    predictionColumn -> "cluster",
    seed             -> 123.0,
    tolerance        -> 1e-7,
    initMode         -> KMeans.ParallelInitMode(),
    initSteps        -> 8
  )

}
