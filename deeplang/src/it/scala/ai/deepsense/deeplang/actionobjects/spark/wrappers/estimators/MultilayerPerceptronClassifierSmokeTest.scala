package ai.deepsense.deeplang.actionobjects.spark.wrappers.estimators

import ai.deepsense.deeplang.parameters.ParamPair
import ai.deepsense.deeplang.parameters.selections.NameSingleColumnSelection

class MultilayerPerceptronClassifierSmokeTest extends AbstractEstimatorModelWrapperSmokeTest {

  override def className: String = "MultilayerPerceptronClassifier"

  override val estimator = new MultilayerPerceptronClassifier()

  import estimator._

  override val estimatorParams: Seq[ParamPair[_]] = Seq(
    featuresColumn   -> NameSingleColumnSelection("myFeatures"),
    labelColumn      -> NameSingleColumnSelection("myRating"),
    layersParam      -> Array(3.0, 2.0, 1.0),
    maxIterations    -> 120.0,
    predictionColumn -> "prediction",
    seed             -> 100.0,
    tolerance        -> 2e-5
  )

}
