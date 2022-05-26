package io.deepsense.deeplang.doperables

import io.deepsense.deeplang.doperables.dataframe.DataFrame
import io.deepsense.deeplang.doperables.serialization.SerializableSparkModel
import io.deepsense.deeplang.inference.exceptions.SparkTransformSchemaException
import io.deepsense.deeplang.params.ParamMap
import io.deepsense.deeplang.DeeplangTestSupport
import io.deepsense.deeplang.ExecutionContext
import io.deepsense.deeplang.UnitSpec

class SparkModelWrapperSpec extends UnitSpec with DeeplangTestSupport {

  import EstimatorModelWrappersFixtures._

  "SparkModelWrapper" should {
    "ignore default parameter values" in {
      val wrapper = new ExampleSparkModelWrapper()
      wrapper.extractParamMap() shouldBe ParamMap.empty
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
      val e = intercept[SparkTransformSchemaException] {
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
