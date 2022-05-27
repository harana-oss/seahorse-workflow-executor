package ai.deepsense.deeplang.actionobjects.spark.wrappers.estimators

import ai.deepsense.deeplang.actionobjects.multicolumn.SingleColumnParams.SingleTransformInPlaceChoices.NoInPlaceChoice
import ai.deepsense.deeplang.parameters.ParamPair
import ai.deepsense.deeplang.parameters.selections.NameSingleColumnSelection

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
