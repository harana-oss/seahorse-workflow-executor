package ai.deepsense.deeplang.doperables.spark.wrappers.estimators

import ai.deepsense.deeplang.doperables.multicolumn.SingleColumnParams.SingleTransformInPlaceChoices.NoInPlaceChoice
import ai.deepsense.deeplang.params.ParamPair
import ai.deepsense.deeplang.params.selections.NameSingleColumnSelection

class IDFEstimatorSmokeTest extends AbstractEstimatorModelWrapperSmokeTest {

  override def className: String = "IDF"

  override val estimator = new IDFEstimator()

  import estimator._

  override val estimatorParams: Seq[ParamPair[_]] = Seq(
    minDocFreq          -> 0,
    inputColumn         -> NameSingleColumnSelection("myFeatures"),
    singleInPlaceChoice -> NoInPlaceChoice().setOutputColumn("testOutputColumn")
  )

}
