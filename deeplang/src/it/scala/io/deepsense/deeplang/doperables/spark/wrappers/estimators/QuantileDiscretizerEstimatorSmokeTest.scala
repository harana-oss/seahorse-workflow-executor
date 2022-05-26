package io.deepsense.deeplang.doperables.spark.wrappers.estimators

import io.deepsense.deeplang.doperables.multicolumn.SingleColumnParams.SingleTransformInPlaceChoices.NoInPlaceChoice
import io.deepsense.deeplang.params.ParamPair
import io.deepsense.deeplang.params.selections.NameSingleColumnSelection

class QuantileDiscretizerEstimatorSmokeTest extends AbstractEstimatorModelWrapperSmokeTest {

  override def className: String = "QuantileDiscretizer"

  override val estimator = new QuantileDiscretizerEstimator()

  import estimator._

  override val estimatorParams: Seq[ParamPair[_]] = Seq(
    numBuckets          -> 2,
    inputColumn         -> NameSingleColumnSelection("myRating"),
    singleInPlaceChoice -> NoInPlaceChoice().setOutputColumn("testOutputColumn")
  )

}
