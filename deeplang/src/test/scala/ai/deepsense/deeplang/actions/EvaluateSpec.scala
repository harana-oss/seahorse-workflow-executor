package ai.deepsense.deeplang.actions

import spray.json.JsNumber
import spray.json.JsObject

import ai.deepsense.deeplang._
import ai.deepsense.deeplang.actionobjects.MetricValue
import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.actions.MockActionObjectsFactory._
import ai.deepsense.deeplang.actions.exceptions.TooManyPossibleTypesException
import ai.deepsense.deeplang.exceptions.DeepLangMultiException
import ai.deepsense.deeplang.inference.InferContext
import ai.deepsense.deeplang.inference.InferenceWarnings
import ai.deepsense.deeplang.parameters.ParamsMatchers._

class EvaluateSpec extends UnitSpec with DeeplangTestSupport {

  "Evaluate" should {

    "evaluate input Evaluator on input DataFrame with proper parameters set" in {
      val evaluator = new MockEvaluator

      def testEvaluate(op: Evaluate, expected: MetricValue): Unit = {
        val Vector(outputDataFrame) = op.executeUntyped(Vector(evaluator, mock[DataFrame]))(createExecutionContext)
        outputDataFrame shouldBe expected
      }

      val op1 = Evaluate()
      testEvaluate(op1, metricValue1)

      val paramsForEvaluator = JsObject(evaluator.paramA.name -> JsNumber(2))
      val op2                = Evaluate().setEvaluatorParams(paramsForEvaluator)
      testEvaluate(op2, metricValue2)
    }

    "not modify params in input Evaluator instance upon execution" in {
      val evaluator         = new MockEvaluator
      val originalEvaluator = evaluator.replicate()

      val paramsForEvaluator = JsObject(evaluator.paramA.name -> JsNumber(2))
      val op                 = Evaluate().setEvaluatorParams(paramsForEvaluator)
      op.executeUntyped(Vector(evaluator, mock[DataFrame]))(createExecutionContext)

      evaluator should have(theSameParamsAs(originalEvaluator))
    }

    "infer knowledge from input Evaluator on input DataFrame with proper parameters set" in {
      val evaluator = new MockEvaluator

      def testInference(op: Evaluate, expectedKnowledge: Knowledge[MetricValue]): Unit = {
        val inputDF                    = DataFrame.forInference(createSchema())
        val (knowledge, warnings)      =
          op.inferKnowledgeUntyped(Vector(Knowledge(evaluator), Knowledge(inputDF)))(mock[InferContext])
        // Currently, InferenceWarnings are always empty.
        warnings shouldBe InferenceWarnings.empty
        val Vector(dataFrameKnowledge) = knowledge
        dataFrameKnowledge shouldBe expectedKnowledge
      }

      val op1 = Evaluate()
      testInference(op1, metricValueKnowledge1)

      val paramsForEvaluator = JsObject(evaluator.paramA.name -> JsNumber(2))
      val op2                = Evaluate().setEvaluatorParams(paramsForEvaluator)
      testInference(op2, metricValueKnowledge2)
    }

    "not modify params in input Evaluator instance upon inference" in {
      val evaluator         = new MockEvaluator
      val originalEvaluator = evaluator.replicate()

      val paramsForEvaluator = JsObject(evaluator.paramA.name -> JsNumber(2))
      val op                 = Evaluate().setEvaluatorParams(paramsForEvaluator)
      val inputDF            = DataFrame.forInference(createSchema())
      op.inferKnowledgeUntyped(Vector(Knowledge(evaluator), Knowledge(inputDF)))(mock[InferContext])

      evaluator should have(theSameParamsAs(originalEvaluator))
    }

    "throw Exception" when {
      "there is more than one Evaluator in input Knowledge" in {
        val inputDF    = DataFrame.forInference(createSchema())
        val evaluators = Set[ActionObject](new MockEvaluator, new MockEvaluator)

        val op = Evaluate()
        a[TooManyPossibleTypesException] shouldBe thrownBy {
          op.inferKnowledgeUntyped(Vector(Knowledge(evaluators), Knowledge(inputDF)))(mock[InferContext])
        }
      }
      "values of dynamic parameters are invalid" in {
        val evaluator = new MockEvaluator

        val inputDF = DataFrame.forInference(createSchema())

        val paramsForEvaluator  = JsObject(evaluator.paramA.name -> JsNumber(-2))
        val evaluatorWithParams = Evaluate().setEvaluatorParams(paramsForEvaluator)

        a[DeepLangMultiException] shouldBe thrownBy {
          evaluatorWithParams.inferKnowledgeUntyped(Vector(Knowledge(evaluator), Knowledge(inputDF)))(
            mock[InferContext]
          )
        }
      }
    }
  }

}
