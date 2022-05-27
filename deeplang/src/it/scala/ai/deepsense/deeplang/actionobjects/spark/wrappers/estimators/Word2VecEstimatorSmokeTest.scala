package ai.deepsense.deeplang.actionobjects.spark.wrappers.estimators

import ai.deepsense.deeplang.actionobjects.multicolumn.SingleColumnParams.SingleTransformInPlaceChoices.NoInPlaceChoice
import ai.deepsense.deeplang.parameters.ParamPair
import ai.deepsense.deeplang.parameters.selections.NameSingleColumnSelection

class Word2VecEstimatorSmokeTest extends AbstractEstimatorModelWrapperSmokeTest {

  override def className: String = "Word2Vec"

  override val estimator = new Word2VecEstimator()

  import estimator._

  override val estimatorParams: Seq[ParamPair[_]] = Seq(
    inputColumn         -> NameSingleColumnSelection("myStringFeatures"),
    singleInPlaceChoice -> NoInPlaceChoice().setOutputColumn("testOutputColumn"),
    maxIterations       -> 2,
    stepSize            -> 0.25,
    seed                -> 42,
    vectorSize          -> 99,
    numPartitions       -> 4,
    minCount            -> 1
  )

}
