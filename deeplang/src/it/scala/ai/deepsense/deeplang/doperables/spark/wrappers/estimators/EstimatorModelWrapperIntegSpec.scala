package ai.deepsense.deeplang.doperables.spark.wrappers.estimators

import ai.deepsense.deeplang.DeeplangIntegTestSupport
import ai.deepsense.deeplang.doperables.dataframe.DataFrame
import org.apache.spark.sql.Row
import org.apache.spark.sql.types.IntegerType
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.types.StructField

class EstimatorModelWrapperIntegSpec extends DeeplangIntegTestSupport {

  import ai.deepsense.deeplang.doperables.spark.wrappers.estimators.EstimatorModelWrapperFixtures._

  val inputDF = {
    val rowSeq = Seq(Row(1), Row(2), Row(3))
    val schema = StructType(Seq(StructField("x", IntegerType, nullable = false)))
    createDataFrame(rowSeq, schema)
  }

  val estimatorPredictionParamValue = "estimatorPrediction"

  val expectedSchema = StructType(
    Seq(
      StructField("x", IntegerType, nullable = false),
      StructField(estimatorPredictionParamValue, IntegerType, nullable = false)
    )
  )

  val transformerPredictionParamValue = "modelPrediction"

  val expectedSchemaForTransformerParams = StructType(
    Seq(
      StructField("x", IntegerType, nullable = false),
      StructField(transformerPredictionParamValue, IntegerType, nullable = false)
    )
  )

  "EstimatorWrapper" should {
    "_fit() and transform() + transformSchema() with parameters inherited" in {

      val transformer = createEstimatorAndFit()

      val transformOutputSchema =
        transformer._transform(executionContext, inputDF).sparkDataFrame.schema
      transformOutputSchema shouldBe expectedSchema

      val inferenceOutputSchema = transformer._transformSchema(inputDF.sparkDataFrame.schema)
      inferenceOutputSchema shouldBe Some(expectedSchema)
    }

    "_fit() and transform() + transformSchema() with parameters overwritten" in {

      val transformer = createEstimatorAndFit().setPredictionColumn(transformerPredictionParamValue)

      val transformOutputSchema =
        transformer._transform(executionContext, inputDF).sparkDataFrame.schema
      transformOutputSchema shouldBe expectedSchemaForTransformerParams

      val inferenceOutputSchema = transformer._transformSchema(inputDF.sparkDataFrame.schema)
      inferenceOutputSchema shouldBe Some(expectedSchemaForTransformerParams)
    }

    "_fit_infer().transformSchema() with parameters inherited" in {

      val estimatorWrapper = new SimpleSparkEstimatorWrapper()
        .setPredictionColumn(estimatorPredictionParamValue)

      estimatorWrapper
        ._fit_infer(inputDF.schema)
        ._transformSchema(inputDF.sparkDataFrame.schema) shouldBe Some(expectedSchema)
    }

    "_fit_infer().transformSchema() with parameters overwritten" in {

      val estimatorWrapper      = new SimpleSparkEstimatorWrapper()
        .setPredictionColumn(estimatorPredictionParamValue)
      val transformer           =
        estimatorWrapper._fit_infer(inputDF.schema).asInstanceOf[SimpleSparkModelWrapper]
      val transformerWithParams = transformer.setPredictionColumn(transformerPredictionParamValue)

      val outputSchema = transformerWithParams._transformSchema(inputDF.sparkDataFrame.schema)
      outputSchema shouldBe Some(expectedSchemaForTransformerParams)
    }
  }

  private def createEstimatorAndFit(): SimpleSparkModelWrapper = {

    val estimatorWrapper = new SimpleSparkEstimatorWrapper()
      .setPredictionColumn(estimatorPredictionParamValue)

    val transformer =
      estimatorWrapper._fit(executionContext, inputDF).asInstanceOf[SimpleSparkModelWrapper]
    transformer.getPredictionColumn() shouldBe estimatorPredictionParamValue

    transformer
  }

}
