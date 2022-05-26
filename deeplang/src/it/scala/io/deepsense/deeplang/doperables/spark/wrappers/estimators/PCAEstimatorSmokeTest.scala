package io.deepsense.deeplang.doperables.spark.wrappers.estimators

import io.deepsense.deeplang.doperables.multicolumn.SingleColumnParams.SingleTransformInPlaceChoices.NoInPlaceChoice
import io.deepsense.deeplang.params.ParamPair
import io.deepsense.deeplang.params.selections.NameSingleColumnSelection

class PCAEstimatorSmokeTest extends AbstractEstimatorModelWrapperSmokeTest {

  override def className: String = "PCA"

  override val estimator = new PCAEstimator()

  import estimator._

  override val estimatorParams: Seq[ParamPair[_]] = Seq(
    k                   -> 2,
    inputColumn         -> NameSingleColumnSelection("myFeatures"),
    singleInPlaceChoice -> NoInPlaceChoice().setOutputColumn("testOutputColumn")
  )

}
