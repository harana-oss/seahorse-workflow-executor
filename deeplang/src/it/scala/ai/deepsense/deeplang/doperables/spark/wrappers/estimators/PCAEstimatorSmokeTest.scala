package ai.deepsense.deeplang.doperables.spark.wrappers.estimators

import ai.deepsense.deeplang.doperables.multicolumn.SingleColumnParams.SingleTransformInPlaceChoices.NoInPlaceChoice
import ai.deepsense.deeplang.params.ParamPair
import ai.deepsense.deeplang.params.selections.NameSingleColumnSelection

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
