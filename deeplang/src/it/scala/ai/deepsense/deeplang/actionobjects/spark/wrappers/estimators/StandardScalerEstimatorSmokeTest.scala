package ai.deepsense.deeplang.actionobjects.spark.wrappers.estimators

import ai.deepsense.deeplang.actionobjects.multicolumn.SingleColumnParams.SingleTransformInPlaceChoices.NoInPlaceChoice
import ai.deepsense.deeplang.parameters.ParamPair
import ai.deepsense.deeplang.parameters.selections.NameSingleColumnSelection

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
