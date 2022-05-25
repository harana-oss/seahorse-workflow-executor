package io.deepsense.deeplang.doperables.spark.wrappers.estimators

import io.deepsense.deeplang.doperables.multicolumn.SingleColumnParams.SingleTransformInPlaceChoices.NoInPlaceChoice
import io.deepsense.deeplang.params.ParamPair
import io.deepsense.deeplang.params.selections.NameSingleColumnSelection

class VectorIndexerEstimatorSmokeTest
  extends AbstractEstimatorModelWrapperSmokeTest {

  override def className: String = "VectorIndexer"

  override val estimator = new VectorIndexerEstimator()

  import estimator._

  override val estimatorParams: Seq[ParamPair[_]] = Seq(
    maxCategories -> 2,
    inputColumn -> NameSingleColumnSelection("myFeatures"),
    singleInPlaceChoice -> NoInPlaceChoice().setOutputColumn("testOutputColumn")
  )
}
