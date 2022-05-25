package io.deepsense.deeplang.doperables.spark.wrappers.estimators

import io.deepsense.deeplang.doperables.multicolumn.SingleColumnParams.SingleTransformInPlaceChoices.NoInPlaceChoice
import io.deepsense.deeplang.params.ParamPair
import io.deepsense.deeplang.params.selections.NameSingleColumnSelection

class Word2VecEstimatorSmokeTest
  extends AbstractEstimatorModelWrapperSmokeTest {

  override def className: String = "Word2Vec"

  override val estimator = new Word2VecEstimator()

  import estimator._

  override val estimatorParams: Seq[ParamPair[_]] = Seq(
    inputColumn -> NameSingleColumnSelection("myStringFeatures"),
    singleInPlaceChoice -> NoInPlaceChoice().setOutputColumn("testOutputColumn"),
    maxIterations -> 2,
    stepSize -> 0.25,
    seed -> 42,
    vectorSize -> 99,
    numPartitions -> 4,
    minCount -> 1
  )
}
