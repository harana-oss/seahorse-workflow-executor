package io.deepsense.deeplang.doperables.spark.wrappers.estimators

import io.deepsense.deeplang.params.ParamPair
import io.deepsense.deeplang.params.selections.NameSingleColumnSelection

class ALSSmokeTest extends AbstractEstimatorModelWrapperSmokeTest {

  override def className: String = "ALS"

  override val estimator = new ALS()

  import estimator._

  override val estimatorParams: Seq[ParamPair[_]] = Seq(
    alpha               -> 1.0,
    checkpointInterval  -> 15.0,
    implicitPrefs       -> false,
    itemColumn          -> NameSingleColumnSelection("myItemId"),
    maxIterations       -> 5.0,
    nonnegative         -> false,
    numItemBlocks       -> 10.0,
    numUserBlocks       -> 10.0,
    predictionColumn    -> "prediction",
    rank                -> 8.0,
    ratingColumn        -> NameSingleColumnSelection("myRating"),
    regularizationParam -> 0.2,
    seed                -> 100.0,
    userColumn          -> NameSingleColumnSelection("myUserId")
  )

}
