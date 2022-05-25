package io.deepsense.deeplang.doperables.spark.wrappers.estimators

import io.deepsense.deeplang.doperables.multicolumn.SingleColumnParams.SingleTransformInPlaceChoices.NoInPlaceChoice
import io.deepsense.deeplang.params.ParamPair
import io.deepsense.deeplang.params.selections.NameSingleColumnSelection

class MinMaxScalerEstimatorSmokeTest
  extends AbstractEstimatorModelWrapperSmokeTest {

  override def className: String = "MinMaxScaler"

  override val estimator = new MinMaxScalerEstimator()

  import estimator._

  override val estimatorParams: Seq[ParamPair[_]] = Seq(
    min -> 0.0,
    max -> 1.0,
    inputColumn -> NameSingleColumnSelection("myFeatures"),
    singleInPlaceChoice -> NoInPlaceChoice().setOutputColumn("testOutputColumn")
  )
}
