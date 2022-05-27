package ai.deepsense.deeplang.actions

import spray.json.JsNumber
import spray.json.JsObject

import ai.deepsense.deeplang._
import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.actions.MockActionObjectsFactory._
import ai.deepsense.deeplang.actions.MockTransformers._
import ai.deepsense.deeplang.exceptions.DeepLangMultiException
import ai.deepsense.deeplang.inference.InferContext
import ai.deepsense.deeplang.inference.InferenceWarnings
import ai.deepsense.deeplang.parameters.ParamsMatchers._

class TransformSpec extends UnitSpec with DeeplangTestSupport {

  "Transform" should {

    "transform input Transformer on input DataFrame with proper parameters set" in {
      val transformer = new MockTransformer

      def testTransform(op: Transform, expectedDataFrame: DataFrame): Unit = {
        val Vector(outputDataFrame) = op.executeUntyped(Vector(transformer, createDataFrame()))(createExecutionContext)
        outputDataFrame shouldBe expectedDataFrame
      }

      val op1 = Transform()
      testTransform(op1, outputDataFrame1)

      val paramsForTransformer = JsObject(transformer.paramA.name -> JsNumber(2))
      val op2                  = Transform().setTransformerParams(paramsForTransformer)
      testTransform(op2, outputDataFrame2)
    }

    "not modify params in input Transformer instance upon execution" in {
      val transformer         = new MockTransformer
      val originalTransformer = transformer.replicate()

      val paramsForTransformer = JsObject(transformer.paramA.name -> JsNumber(2))
      val op                   = Transform().setTransformerParams(paramsForTransformer)
      op.executeUntyped(Vector(transformer, mock[DataFrame]))(createExecutionContext)

      transformer should have(theSameParamsAs(originalTransformer))
    }

    "infer knowledge from input Transformer on input DataFrame with proper parameters set" in {
      val transformer = new MockTransformer

      def testInference(op: Transform, expecteDataFrameKnowledge: Knowledge[DataFrame]): Unit = {
        val inputDF                    = createDataFrame()
        val (knowledge, warnings)      =
          op.inferKnowledgeUntyped(Vector(Knowledge(transformer), Knowledge(inputDF)))(mock[InferContext])
        // Currently, InferenceWarnings are always empty.
        warnings shouldBe InferenceWarnings.empty
        val Vector(dataFrameKnowledge) = knowledge
        dataFrameKnowledge shouldBe expecteDataFrameKnowledge
      }

      val op1 = Transform()
      testInference(op1, dataFrameKnowledge(outputSchema1))

      val paramsForTransformer = JsObject(transformer.paramA.name -> JsNumber(2))
      val op2                  = Transform().setTransformerParams(paramsForTransformer)
      testInference(op2, dataFrameKnowledge(outputSchema2))
    }

    "not modify params in input Transformer instance upon inference" in {
      val transformer         = new MockTransformer
      val originalTransformer = transformer.replicate()

      val paramsForTransformer = JsObject(transformer.paramA.name -> JsNumber(2))
      val op                   = Transform().setTransformerParams(paramsForTransformer)
      val inputDF              = DataFrame.forInference(createSchema())
      op.inferKnowledgeUntyped(Vector(Knowledge(transformer), Knowledge(inputDF)))(mock[InferContext])

      transformer should have(theSameParamsAs(originalTransformer))
    }

    "infer knowledge even if there is more than one Transformer in input Knowledge" in {
      val inputDF      = DataFrame.forInference(createSchema())
      val transformers = Set[ActionObject](new MockTransformer, new MockTransformer)

      val op                    = Transform()
      val (knowledge, warnings) =
        op.inferKnowledgeUntyped(Vector(Knowledge(transformers), Knowledge(inputDF)))(mock[InferContext])

      knowledge shouldBe Vector(Knowledge(DataFrame.forInference()))
      warnings shouldBe InferenceWarnings.empty
    }

    "throw Exception" when {
      "Transformer's dynamic parameters are invalid" in {
        val inputDF     = DataFrame.forInference(createSchema())
        val transformer = new MockTransformer
        val transform   = Transform().setTransformerParams(JsObject(transformer.paramA.name -> JsNumber(-2)))

        a[DeepLangMultiException] shouldBe thrownBy {
          transform.inferKnowledgeUntyped(Vector(Knowledge(transformer), Knowledge(inputDF)))(mock[InferContext])
        }
      }
    }
  }

}
