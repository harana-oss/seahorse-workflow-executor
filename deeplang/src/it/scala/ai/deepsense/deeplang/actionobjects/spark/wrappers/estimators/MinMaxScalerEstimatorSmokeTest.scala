package ai.deepsense.deeplang.actionobjects.spark.wrappers.estimators

import ai.deepsense.deeplang.actionobjects.multicolumn.SingleColumnParams.SingleTransformInPlaceChoices.NoInPlaceChoice
import ai.deepsense.deeplang.parameters.ParamPair
import ai.deepsense.deeplang.parameters.selections.NameSingleColumnSelection

class MinMaxScalerEstimatorSmokeTest extends AbstractEstimatorModelWrapperSmokeTest {

  override def className: String = "MinMaxScaler"

  override val estimator = new MinMaxScalerEstimator()

  import estimator._

  override val estimatorParams: Seq[ParamPair[_]] = Seq(
    min                 -> 0.0,
    max                 -> 1.0,
    inputColumn         -> NameSingleColumnSelection("myFeatures"),
    singleInPlaceChoice -> NoInPlaceChoice().setOutputColumn("testOutputColumn")
  )

}
