package ai.deepsense.deeplang.actionobjects.spark.wrappers.estimators

import ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common.ClassificationImpurity.Gini
import ai.deepsense.deeplang.parameters.ParamPair
import ai.deepsense.deeplang.parameters.selections.NameSingleColumnSelection

class DecisionTreeClassifierSmokeTest extends AbstractEstimatorModelWrapperSmokeTest {

  override def className: String = "DecisionTreeClassifier"

  override val estimator = new DecisionTreeClassifier()

  import estimator.vanillaDecisionTreeClassifier._

  override val estimatorParams: Seq[ParamPair[_]] = Seq(
    maxDepth            -> 6.0,
    maxBins             -> 28.0,
    minInstancesPerNode -> 2.0,
    minInfoGain         -> 0.05,
    maxMemoryInMB       -> 312.0,
    cacheNodeIds        -> false,
    checkpointInterval  -> 8.0,
    seed                -> 12345.0,
    impurity            -> Gini(),
    featuresColumn      -> NameSingleColumnSelection("myFeatures"),
    labelColumn         -> NameSingleColumnSelection("myLabel"),
    probabilityColumn   -> "prob",
    rawPredictionColumn -> "rawPred",
    predictionColumn    -> "pred"
  )

}
