package io.deepsense.deeplang.doperables.spark.wrappers.estimators

import io.deepsense.deeplang.doperables.spark.wrappers.estimators.LDA.OnlineLDAOptimizer
import io.deepsense.deeplang.params.ParamPair
import io.deepsense.deeplang.params.selections.NameSingleColumnSelection

class LDASmokeTest extends AbstractEstimatorModelWrapperSmokeTest {

  override def className: String = "LDA"

  override val estimator = new LDA()

  import estimator._

  override val estimatorParams: Seq[ParamPair[_]] = Seq(
    checkpointInterval -> 4.0,
    featuresColumn     -> NameSingleColumnSelection("myFeatures"),
    k                  -> 3.0,
    maxIterations      -> 30.0,
    optimizer -> OnlineLDAOptimizer()
      .setDocConcentration(Array(0.5, 0.3, 0.2))
      .setTopicConcentration(0.8),
    seed                    -> 123.0,
    subsamplingRate         -> 0.1,
    topicDistributionColumn -> "cluster"
  )

  override def isAlgorithmDeterministic: Boolean = false

}
