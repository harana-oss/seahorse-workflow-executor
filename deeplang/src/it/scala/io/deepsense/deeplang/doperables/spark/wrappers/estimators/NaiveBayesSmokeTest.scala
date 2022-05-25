package io.deepsense.deeplang.doperables.spark.wrappers.estimators

import io.deepsense.deeplang.doperables.spark.wrappers.estimators.NaiveBayes.Multinomial
import io.deepsense.deeplang.params.ParamPair
import io.deepsense.deeplang.params.selections.NameSingleColumnSelection

class NaiveBayesSmokeTest extends AbstractEstimatorModelWrapperSmokeTest {

  override def className: String = "NaiveBayes"

  override val estimator = new NaiveBayes()

  import estimator._

  override val estimatorParams: Seq[ParamPair[_]] = Seq(
    smoothing -> 1.0,
    modelType -> Multinomial(),
    featuresColumn -> NameSingleColumnSelection("myFeatures"),
    labelColumn -> NameSingleColumnSelection("myLabel"),
    probabilityColumn -> "prob",
    rawPredictionColumn -> "rawPred",
    predictionColumn -> "pred"
  )
}
