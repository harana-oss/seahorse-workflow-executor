package ai.deepsense.deeplang.actionobjects.spark.wrappers.estimators

import ai.deepsense.deeplang.actionobjects.spark.wrappers.params.common.OptionalWeightColumnChoice
import ai.deepsense.deeplang.parameters.ParamPair
import ai.deepsense.deeplang.parameters.selections.NameSingleColumnSelection

class IsotonicRegressionWithWeightsSmokeTest extends AbstractEstimatorModelWrapperSmokeTest {

  override def className: String = "IsotonicRegression"

  override val estimator = new IsotonicRegression()

  import estimator._

  val weightColumnName = "myWeight"

  val weightColumnChoice = OptionalWeightColumnChoice
    .WeightColumnYesOption()
    .setWeightColumn(NameSingleColumnSelection(weightColumnName))

  override val estimatorParams: Seq[ParamPair[_]] = Seq(
    featureIndex         -> 1,
    featuresColumn       -> NameSingleColumnSelection("myFeatures"),
    isotonic             -> true,
    labelColumn          -> NameSingleColumnSelection("myLabel"),
    predictionColumn     -> "isotonicPrediction",
    optionalWeightColumn -> weightColumnChoice
  )

  className should {
    "pass weight column value to wrapped model" in {
      val estimatorWithParams = estimator.set(estimatorParams: _*)
      val sparkEstimator      = estimatorWithParams.sparkEstimator
      val sparkParamMap       = estimatorWithParams.sparkParamMap(sparkEstimator, dataFrame.sparkDataFrame.schema)
      sparkParamMap.get(estimator.sparkEstimator.weightCol) shouldBe Some(weightColumnName)
    }
  }

}
