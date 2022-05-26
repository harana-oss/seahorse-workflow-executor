package io.deepsense.deeplang.doperables.spark.wrappers.estimators

import io.deepsense.deeplang.doperables.spark.wrappers.params.common.OptionalWeightColumnChoice
import io.deepsense.deeplang.params.ParamPair
import io.deepsense.deeplang.params.selections.NameSingleColumnSelection

class LogisticRegressionSmokeTest extends AbstractEstimatorModelWrapperSmokeTest {

  override def className: String = "LogisticRegression"

  override val estimator = new LogisticRegression()

  import estimator._

  val weightColumnChoice = OptionalWeightColumnChoice
    .WeightColumnYesOption()
    .setWeightColumn(NameSingleColumnSelection("myWeight"))

  override val estimatorParams: Seq[ParamPair[_]] = Seq(
    elasticNetParam      -> 0.8,
    fitIntercept         -> true,
    maxIterations        -> 2.0,
    regularizationParam  -> 0.1,
    tolerance            -> 0.01,
    standardization      -> true,
    featuresColumn       -> NameSingleColumnSelection("myFeatures"),
    labelColumn          -> NameSingleColumnSelection("myLabel"),
    probabilityColumn    -> "prob",
    rawPredictionColumn  -> "rawPred",
    predictionColumn     -> "pred",
    threshold            -> 0.3,
    optionalWeightColumn -> weightColumnChoice
  )

}
