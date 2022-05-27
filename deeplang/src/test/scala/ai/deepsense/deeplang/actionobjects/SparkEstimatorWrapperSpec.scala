package ai.deepsense.deeplang.actionobjects

import ai.deepsense.deeplang.DeeplangTestSupport
import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.UnitSpec

class SparkEstimatorWrapperSpec extends UnitSpec with DeeplangTestSupport {

  import EstimatorModelWrappersFixtures._

  "SparkEstimatorWrapper" should {
    "fit a DataFrame" in {
      val wrapper        = new ExampleSparkEstimatorWrapper().setNumericParamWrapper(paramValueToSet)
      val inputDataFrame = createDataFrame()
      val modelWrapper   =
        wrapper._fit(mock[ExecutionContext], inputDataFrame)
      modelWrapper.sparkModel shouldBe fitModel
    }
    "infer knowledge when schema is provided" in {
      val wrapper              = new ExampleSparkEstimatorWrapper().setNumericParamWrapper(paramValueToSet)
      val inferredModelWrapper = wrapper
        ._fit_infer(Some(createSchema()))
        .asInstanceOf[ExampleSparkModelWrapper]
      inferredModelWrapper.parentEstimator.serializableEstimator shouldBe wrapper.serializableEstimator
    }
    "infer knowledge when schema isn't provided" in {
      val wrapper              = new ExampleSparkEstimatorWrapper()
      val inferredModelWrapper = wrapper._fit_infer(None)
      inferredModelWrapper.parentEstimator.serializableEstimator shouldBe wrapper.serializableEstimator
    }
  }

}
