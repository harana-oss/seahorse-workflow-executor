package ai.deepsense.deeplang.doperables.spark.wrappers.estimators

import ai.deepsense.deeplang.doperables.spark.wrappers.params.common.OptionalWeightColumnChoice
import ai.deepsense.deeplang.params.ParamPair
import ai.deepsense.deeplang.params.selections.NameSingleColumnSelection

class IsotonicRegressionWithoutWeightsSmokeTest extends AbstractEstimatorModelWrapperSmokeTest {

  override def className: String = "IsotonicRegression"

  override val estimator = new IsotonicRegression()

  import estimator._

  override val estimatorParams: Seq[ParamPair[_]] = Seq(
    featureIndex         -> 1,
    featuresColumn       -> NameSingleColumnSelection("myFeatures"),
    isotonic             -> true,
    labelColumn          -> NameSingleColumnSelection("myLabel"),
    predictionColumn     -> "isotonicPrediction",
    optionalWeightColumn -> OptionalWeightColumnChoice.WeightColumnNoOption()
  )

  className should {
    "pass no weight column value to wrapped model" in {
      val estimatorWithParams = estimator.set(estimatorParams: _*)
      val sparkEstimator      = estimatorWithParams.sparkEstimator
      val sparkParamMap       = estimatorWithParams.sparkParamMap(sparkEstimator, dataFrame.sparkDataFrame.schema)
      sparkParamMap.get(estimator.sparkEstimator.weightCol) shouldBe None
    }
  }

}
