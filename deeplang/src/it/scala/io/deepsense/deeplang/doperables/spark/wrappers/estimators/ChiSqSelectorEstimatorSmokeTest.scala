package io.deepsense.deeplang.doperables.spark.wrappers.estimators

import io.deepsense.deeplang.params.ParamPair
import io.deepsense.deeplang.params.selections.NameSingleColumnSelection

class ChiSqSelectorEstimatorSmokeTest extends AbstractEstimatorModelWrapperSmokeTest {

  override def className: String = "ChiSqSelector"

  override val estimator = new ChiSqSelectorEstimator()

  import estimator._

  override val estimatorParams: Seq[ParamPair[_]] = Seq(
    numTopFeatures -> 2,
    featuresColumn -> NameSingleColumnSelection("myFeatures"),
    labelColumn    -> NameSingleColumnSelection("myLabel"),
    outputColumn   -> "output"
  )

}
