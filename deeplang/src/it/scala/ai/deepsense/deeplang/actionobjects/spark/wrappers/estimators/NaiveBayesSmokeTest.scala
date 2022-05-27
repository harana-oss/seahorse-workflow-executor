package ai.deepsense.deeplang.actionobjects.spark.wrappers.estimators

import ai.deepsense.deeplang.actionobjects.spark.wrappers.estimators.NaiveBayes.Multinomial
import ai.deepsense.deeplang.parameters.ParamPair
import ai.deepsense.deeplang.parameters.selections.NameSingleColumnSelection

class NaiveBayesSmokeTest extends AbstractEstimatorModelWrapperSmokeTest {

  override def className: String = "NaiveBayes"

  override val estimator = new NaiveBayes()

  import estimator._

  override val estimatorParams: Seq[ParamPair[_]] = Seq(
    smoothing           -> 1.0,
    modelType           -> Multinomial(),
    featuresColumn      -> NameSingleColumnSelection("myFeatures"),
    labelColumn         -> NameSingleColumnSelection("myLabel"),
    probabilityColumn   -> "prob",
    rawPredictionColumn -> "rawPred",
    predictionColumn    -> "pred"
  )

}
