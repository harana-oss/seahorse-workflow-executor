package io.deepsense.deeplang.doperables

import org.apache.spark.SparkContext
import org.apache.spark.sql.types._
import org.mockito.ArgumentMatchers._
import org.mockito.Mockito.when
import org.mockito.invocation.InvocationOnMock
import org.mockito.stubbing.Answer
import spray.json.JsObject

import io.deepsense.deeplang._
import io.deepsense.deeplang.doperables.InnerWorkflowTestFactory._
import io.deepsense.deeplang.doperables.dataframe.DataFrame
import io.deepsense.deeplang.doperations.ConvertType
import io.deepsense.deeplang.inference.InferContext
import io.deepsense.deeplang.params.Param
import io.deepsense.deeplang.params.custom.InnerWorkflow
import io.deepsense.deeplang.params.custom.PublicParam
import io.deepsense.sparkutils.SparkSQLSession

class CustomTransformerSpec extends UnitSpec {

  "CustomTransfromer" should {

    "execute inner workflow" in {
      val workflow        = InnerWorkflow(simpleGraph(), JsObject())
      val outputDataFrame = mock[DataFrame]

      val innerWorkflowExecutor = mock[InnerWorkflowExecutor]
      when(innerWorkflowExecutor.execute(any(), same(workflow), any())).thenReturn(outputDataFrame)

      val context = mockExecutionContext(innerWorkflowExecutor)

      val transformer = new CustomTransformer(workflow, Seq.empty)

      transformer._transform(context, mock[DataFrame]) shouldBe outputDataFrame
    }

    "infer knowledge" in {
      val inferContext = MockedInferContext()

      val transformer = new CustomTransformer(InnerWorkflow(simpleGraph(), JsObject()), Seq.empty)

      val schema = StructType(
        Seq(
          StructField("column1", DoubleType),
          StructField("column2", DoubleType)
        )
      )

      val expectedSchema = StructType(
        Seq(
          StructField("column1", StringType),
          StructField("column2", DoubleType)
        )
      )

      transformer._transformSchema(schema, inferContext) shouldBe Some(expectedSchema)
    }

    "replicate" in {
      val publicParam             = TypeConverter().targetType.replicate("public name")
      val publicParamsWithValues  = Seq(ParamWithValues(publicParam))
      val params: Array[Param[_]] = publicParamsWithValues.map(_.param).toArray

      val workflow =
        InnerWorkflow(simpleGraph(), JsObject(), List(PublicParam(innerNodeId, "target type", "public name")))
      val transformer = new CustomTransformer(workflow, publicParamsWithValues)

      val replicated = transformer.replicate()
      replicated.innerWorkflow shouldBe workflow
      replicated.params shouldBe params
    }

    "replicate with set values" in {
      val publicParam  = TypeConverter().targetType.replicate("public name")
      val defaultValue = TargetTypeChoices.IntegerTargetTypeChoice()
      val setValue     = TargetTypeChoices.StringTargetTypeChoice()
      val publicParamsWithValues =
        Seq(ParamWithValues(publicParam, Some(defaultValue), Some(setValue)))
      val params: Array[Param[_]] = publicParamsWithValues.map(_.param).toArray

      val workflow =
        InnerWorkflow(simpleGraph(), JsObject(), List(PublicParam(innerNodeId, "target type", "public name")))
      val transformer = new CustomTransformer(workflow, publicParamsWithValues)

      val replicated = transformer.replicate()
      replicated.innerWorkflow shouldBe workflow
      replicated.params shouldBe params
      replicated.getDefault(publicParam) shouldBe Some(defaultValue)
      replicated.get(publicParam) shouldBe Some(setValue)
    }

    "set public params" when {
      "executing inner workflow" in {
        val innerParam             = TypeConverter().targetType
        val publicParam            = TypeConverter().targetType.replicate("public name")
        val publicParamsWithValues = Seq(ParamWithValues(publicParam))

        val customTargetType = TargetTypeChoices.LongTargetTypeChoice()

        val workflow =
          InnerWorkflow(simpleGraph(), JsObject(), List(PublicParam(innerNodeId, "target type", "public name")))

        val outputDataFrame = mock[DataFrame]

        val innerWorkflowExecutor = mock[InnerWorkflowExecutor]
        when(innerWorkflowExecutor.execute(any(), any(), any()))
          .thenAnswer(new Answer[DataFrame] {
            override def answer(invocation: InvocationOnMock): DataFrame = {
              val workflow = invocation.getArguments.apply(1).asInstanceOf[InnerWorkflow]
              val innerOp = workflow.graph.nodes
                .find(_.id.toString == innerNodeId)
                .get
                .value
                .asInstanceOf[ConvertType]

              innerOp.get(innerParam) shouldBe Some(customTargetType)

              outputDataFrame
            }
          })

        val context = mockExecutionContext(innerWorkflowExecutor)

        val transformer = new CustomTransformer(workflow, publicParamsWithValues)
        transformer.set(publicParam -> customTargetType)

        transformer._transform(context, mock[DataFrame]) shouldBe outputDataFrame
      }

      "inferring schema" in {
        val inferContext = MockedInferContext()

        val publicParam            = TypeConverter().targetType.replicate("public name")
        val publicParamsWithValues = Seq(ParamWithValues(publicParam))

        val transformer =
          new CustomTransformer(
            InnerWorkflow(simpleGraph(), JsObject(), List(PublicParam(innerNodeId, "target type", "public name"))),
            publicParamsWithValues
          )

        transformer.set(publicParam -> TargetTypeChoices.LongTargetTypeChoice())

        val schema = StructType(
          Seq(
            StructField("column1", DoubleType),
            StructField("column2", DoubleType)
          )
        )

        val expectedSchema = StructType(
          Seq(
            StructField("column1", LongType),
            StructField("column2", DoubleType)
          )
        )

        transformer._transformSchema(schema, inferContext) shouldBe Some(expectedSchema)
      }
    }
  }

  private def mockExecutionContext(innerWorkflowExecutor: InnerWorkflowExecutor): ExecutionContext =
    ExecutionContext(
      mock[SparkContext],
      mock[SparkSQLSession],
      mock[InferContext],
      ExecutionMode.Batch,
      mock[FileSystemClient],
      "/tmp",
      "/tmp/library",
      innerWorkflowExecutor,
      mock[ContextualDataFrameStorage],
      None,
      None,
      mock[ContextualCustomCodeExecutor]
    )

}
