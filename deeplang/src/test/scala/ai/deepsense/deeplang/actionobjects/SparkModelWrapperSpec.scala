package ai.deepsense.deeplang.actionobjects

import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.actionobjects.serialization.SerializableSparkModel
import ai.deepsense.deeplang.inference.exceptions.SparkTransformSchemaException
import ai.deepsense.deeplang.parameters.ParameterMap
import ai.deepsense.deeplang.DeeplangTestSupport
import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.UnitSpec

class SparkModelWrapperSpec extends UnitSpec with DeeplangTestSupport {

  import EstimatorModelWrappersFixtures._

  "SparkModelWrapper" should {
    "ignore default parameter values" in {
      val wrapper = new ExampleSparkModelWrapper()
      wrapper.extractParamMap() shouldBe ParameterMap.empty
    }
    "transform a DataFrame" in {
      val wrapper = prepareWrapperWithParams()
      wrapper._transform(mock[ExecutionContext], createDataFrame()) shouldBe
        DataFrame.fromSparkDataFrame(fitDataFrame)
    }
    "transform schema" in {
      val inputSchema = createSchema()
      val wrapper     = prepareWrapperWithParams()
      wrapper._transformSchema(inputSchema) shouldBe Some(transformedSchema)
    }
    "forward an exception thrown by transformSchema wrapped in DeepLangException" in {
      val inputSchema = createSchema()
      val wrapper     = prepareWrapperWithParams()
      wrapper.parentEstimator.sparkEstimator
        .setTransformSchemaShouldThrow(true)
      val e           = intercept[SparkTransformSchemaException] {
        wrapper._transformSchema(inputSchema)
      }
      e.exception shouldBe exceptionThrownByTransformSchema
    }
  }

  private def prepareWrapperWithParams(): ExampleSparkModelWrapper = {
    val model           = new SerializableSparkModel(new ExampleSparkModel())
    val wrapper         = new ExampleSparkModelWrapper().setModel(model)
    val parentEstimator = new ExampleSparkEstimatorWrapper()
    wrapper.setParent(parentEstimator).setNumericParamWrapper(paramValueToSet)
  }

}
