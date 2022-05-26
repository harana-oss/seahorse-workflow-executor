package io.deepsense.deeplang.doperables.spark.wrappers.estimators

import io.deepsense.deeplang.doperables.multicolumn.SingleColumnParams.SingleTransformInPlaceChoices.NoInPlaceChoice
import io.deepsense.deeplang.params.ParamPair
import io.deepsense.deeplang.params.selections.NameSingleColumnSelection

class StandardScalerEstimatorSmokeTest extends AbstractEstimatorModelWrapperSmokeTest {

  override def className: String = "StandardScaler"

  override val estimator = new StandardScalerEstimator()

  import estimator._

  override val estimatorParams: Seq[ParamPair[_]] = Seq(
    withMean            -> false,
    withStd             -> true,
    inputColumn         -> NameSingleColumnSelection("myFeatures"),
    singleInPlaceChoice -> NoInPlaceChoice().setOutputColumn("testOutputColumn")
  )

}
