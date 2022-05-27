package ai.deepsense.deeplang.actionobjects.spark.wrappers.estimators

import ai.deepsense.deeplang.actionobjects.multicolumn.SingleColumnParams.SingleTransformInPlaceChoices.NoInPlaceChoice
import ai.deepsense.deeplang.parameters.ParamPair
import ai.deepsense.deeplang.parameters.selections.NameSingleColumnSelection

class CountVectorizerEstimatorSmokeTest extends AbstractEstimatorModelWrapperSmokeTest {

  override def className: String = "CountVectorizer"

  override val estimator = new CountVectorizerEstimator()

  import estimator._

  override val estimatorParams: Seq[ParamPair[_]] = Seq(
    inputColumn         -> NameSingleColumnSelection("myStringFeatures"),
    singleInPlaceChoice -> NoInPlaceChoice().setOutputColumn("testOutputColumn")
  )

}
