package ai.deepsense.deeplang.doperables

import org.apache.spark.ml
import org.apache.spark.ml.param.BooleanParam
import org.apache.spark.ml.param.DoubleParam
import org.apache.spark.ml.param.ParamMap
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{DataFrame => SparkDataFrame}
import org.apache.spark.sql.Dataset
import org.mockito.Mockito._
import org.scalatestplus.mockito.MockitoSugar

import ai.deepsense.deeplang.doperables.dataframe.DataFrame
import ai.deepsense.deeplang.doperables.report.Report
import ai.deepsense.deeplang.inference.exceptions.SparkTransformSchemaException
import ai.deepsense.deeplang.params.Param
import ai.deepsense.deeplang.params.wrappers.spark.DoubleParamWrapper
import ai.deepsense.deeplang.DeeplangTestSupport
import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.UnitSpec
import ai.deepsense.sparkutils.ML

class SparkTransformerWrapperSpec extends UnitSpec with DeeplangTestSupport {

  import SparkTransformerWrapperSpec._

  "SparkTransformerWrapper" should {
    "transform DataFrame" in {
      val sparkTransformerWrapper =
        ExampleSparkTransformerWrapper().setParamWrapper(paramValueToSet)

      val context        = mock[ExecutionContext]
      val inputDataFrame = createDataFrame()

      sparkTransformerWrapper._transform(context, inputDataFrame) shouldBe
        DataFrame.fromSparkDataFrame(outputDataFrame)
    }
    "infer schema" in {
      val sparkTransformerWrapper =
        ExampleSparkTransformerWrapper().setParamWrapper(paramValueToSet)
      val inputSchema             = createSchema()
      sparkTransformerWrapper._transformSchema(inputSchema) shouldBe
        Some(outputSchema)
    }
    "forward an exception thrown by transformSchema wrapped in DeepLangException" in {
      val inputSchema = createSchema()
      val wrapper     = ExampleSparkTransformerWrapper().setParamWrapper(paramValueToSet)
      wrapper.sparkTransformer.setTransformSchemaShouldThrow(true)
      val e           = intercept[SparkTransformSchemaException] {
        wrapper._transformSchema(inputSchema)
      }
      e.exception shouldBe exceptionThrownByTransformSchema
    }
  }

}

object SparkTransformerWrapperSpec extends MockitoSugar {

  case class ExampleSparkTransformerWrapper() extends SparkTransformerWrapper[ParamValueCheckingTransformer] {

    val paramWrapper = new DoubleParamWrapper[ParamValueCheckingTransformer]("name", Some("description"), _.param)

    setDefault(paramWrapper, 0.0)

    def setParamWrapper(value: Double): this.type = set(paramWrapper, value)

    override val params: Array[Param[_]] = Array(paramWrapper)

    override def report(extended: Boolean = true): Report = ???

  }

  class ParamValueCheckingTransformer extends ML.Transformer {

    def this(id: String) = this()

    val param = new DoubleParam("id", "name", "description")

    override def transformDF(dataset: SparkDataFrame): SparkDataFrame = {
      require($(param) == paramValueToSet)
      outputDataFrame
    }

    val shouldTransformSchemaThrowParam = new BooleanParam("id", "shouldThrow", "description")

    setDefault(shouldTransformSchemaThrowParam, false)

    def setTransformSchemaShouldThrow(b: Boolean): this.type =
      set(shouldTransformSchemaThrowParam, b)

    override def transformSchema(schema: StructType): StructType = {
      if ($(shouldTransformSchemaThrowParam))
        throw exceptionThrownByTransformSchema
      require($(param) == paramValueToSet)
      outputSchema
    }

    override val uid: String = "id"

    override def copy(extra: ParamMap): ml.Transformer =
      defaultCopy(extra)

  }

  val outputSchema = StructType(Seq())

  val outputDataFrame = mock[SparkDataFrame]

  when(outputDataFrame.schema).thenReturn(outputSchema)

  val paramValueToSet = 12.0

  val exceptionThrownByTransformSchema = new Exception("mock exception")

}
