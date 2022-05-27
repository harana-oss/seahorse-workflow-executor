package ai.deepsense.deeplang.actions

import spray.json.JsNumber
import spray.json.JsObject

import ai.deepsense.deeplang.actionobjects.Transformer
import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.actions.MockActionObjectsFactory._
import ai.deepsense.deeplang.actions.exceptions.TooManyPossibleTypesException
import ai.deepsense.deeplang.exceptions.DeepLangMultiException
import ai.deepsense.deeplang.inference.InferContext
import ai.deepsense.deeplang._

class FitPlusTransformSpec extends UnitSpec with DeeplangTestSupport {

  "FitPlusTransform" when {
    "executed" should {
      "pass parameters to the input Estimator produce a Transformer and transformed DataFrame" in {
        val estimator               = new MockEstimator
        val initialParametersValues = estimator.extractParamMap()
        val fpt                     = new FitPlusTransform

        def testExecute(op: FitPlusTransform, expectedDataFrame: DataFrame, expectedTransformer: Transformer): Unit = {
          val results           = op.executeUntyped(Vector(estimator, mock[DataFrame]))(createExecutionContext)
          val outputDataFrame   = results(0).asInstanceOf[DataFrame]
          val outputTransformer = results(1).asInstanceOf[Transformer]

          outputDataFrame shouldBe expectedDataFrame
          outputTransformer shouldBe expectedTransformer
        }

        testExecute(fpt, transformedDataFrame1, transformer1)
        fpt.setEstimatorParams(JsObject(estimator.paramA.name -> JsNumber(2)))
        testExecute(fpt, transformedDataFrame2, transformer2)
        estimator.extractParamMap() shouldBe initialParametersValues
      }

    }
    "inferring knowledge" should {
      "take parameters from the input Estimator, infer Transformer and then a DataFrame" in {
        val estimator               = new MockEstimator
        val initialParametersValues = estimator.extractParamMap()
        val fpt                     = new FitPlusTransform

        def testInference(
                           op: FitPlusTransform,
                           expectedDataFrameKnowledge: Knowledge[DataFrame],
                           expectedTransformerKnowledge: Knowledge[Transformer]
        ): Unit = {
          val (Vector(outputDataFrameKnowledge, outputTransformerKnowledge), _) =
            op.inferKnowledgeUntyped(Vector(Knowledge(estimator), mock[Knowledge[DataFrame]]))(mock[InferContext])

          outputDataFrameKnowledge shouldBe expectedDataFrameKnowledge
          outputTransformerKnowledge shouldBe expectedTransformerKnowledge
        }

        testInference(fpt, transformedDataFrameKnowledge1, transformerKnowledge1)
        fpt.setEstimatorParams(JsObject(estimator.paramA.name -> JsNumber(2)))
        testInference(fpt, transformedDataFrameKnowledge2, transformerKnowledge2)
        estimator.extractParamMap() shouldBe initialParametersValues
      }
      "throw exceptions" when {
        "input Estimator Knowledge consist more than one type" in {
          val estimators                                    = Set[ActionObject](new MockEstimator, new MockEstimator)
          val inputKnowledge: Vector[Knowledge[ActionObject]] =
            Vector(Knowledge(estimators), mock[Knowledge[DataFrame]])
          val fpt                                           = new FitPlusTransform
          a[TooManyPossibleTypesException] shouldBe thrownBy {
            fpt.inferKnowledgeUntyped(inputKnowledge)(mock[InferContext])
          }
        }
        "Estimator's dynamic parameters are invalid" in {
          val estimator                                     = new MockEstimator
          val inputKnowledge: Vector[Knowledge[ActionObject]] =
            Vector(Knowledge(estimator), mock[Knowledge[DataFrame]])
          val fpt                                           = new FitPlusTransform
          fpt.setEstimatorParams(JsObject(estimator.paramA.name -> JsNumber(-2)))
          a[DeepLangMultiException] shouldBe thrownBy {
            fpt.inferKnowledgeUntyped(inputKnowledge)(mock[InferContext])
          }
        }
      }
    }
  }

}
