package io.deepsense.deeplang.doperables.spark.wrappers.estimators

import io.deepsense.deeplang.doperables.multicolumn.SingleColumnParams.SingleTransformInPlaceChoices.NoInPlaceChoice
import io.deepsense.deeplang.params.ParamPair
import io.deepsense.deeplang.params.selections.NameSingleColumnSelection

class CountVectorizerEstimatorSmokeTest extends AbstractEstimatorModelWrapperSmokeTest {

  override def className: String = "CountVectorizer"

  override val estimator = new CountVectorizerEstimator()

  import estimator._

  override val estimatorParams: Seq[ParamPair[_]] = Seq(
    inputColumn         -> NameSingleColumnSelection("myStringFeatures"),
    singleInPlaceChoice -> NoInPlaceChoice().setOutputColumn("testOutputColumn")
  )

}
