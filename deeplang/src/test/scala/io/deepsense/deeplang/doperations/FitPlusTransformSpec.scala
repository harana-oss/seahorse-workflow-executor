package io.deepsense.deeplang.doperations

import spray.json.{JsNumber, JsObject}

import io.deepsense.deeplang.doperables.Transformer
import io.deepsense.deeplang.doperables.dataframe.DataFrame
import io.deepsense.deeplang.doperations.MockDOperablesFactory._
import io.deepsense.deeplang.doperations.exceptions.TooManyPossibleTypesException
import io.deepsense.deeplang.exceptions.DeepLangMultiException
import io.deepsense.deeplang.inference.InferContext
import io.deepsense.deeplang.{DKnowledge, DOperable, ExecutionContext, UnitSpec}

class FitPlusTransformSpec extends UnitSpec {

  "FitPlusTransform" when {
    "executed" should {
      "pass parameters to the input Estimator produce a Transformer and transformed DataFrame" in {
        val estimator = new MockEstimator
        val initialParametersValues = estimator.extractParamMap()
        val fpt = new FitPlusTransform

        def testExecute(
          op: FitPlusTransform,
          expectedDataFrame: DataFrame,
          expectedTransformer: Transformer): Unit = {
          val results = op.executeUntyped(Vector(estimator, mock[DataFrame]))(mock[ExecutionContext])
          val outputDataFrame = results(0).asInstanceOf[DataFrame]
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
        val estimator = new MockEstimator
        val initialParametersValues = estimator.extractParamMap()
        val fpt = new FitPlusTransform

        def testInference(
          op: FitPlusTransform,
          expectedDataFrameKnowledge: DKnowledge[DataFrame],
          expectedTransformerKnowledge: DKnowledge[Transformer]): Unit = {
          val (Vector(outputDataFrameKnowledge, outputTransformerKnowledge), _) =
            op.inferKnowledgeUntyped(Vector(DKnowledge(estimator), mock[DKnowledge[DataFrame]]))(mock[InferContext])

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
          val estimators = Set[DOperable](new MockEstimator, new MockEstimator)
          val inputKnowledge: Vector[DKnowledge[DOperable]] =
            Vector(DKnowledge(estimators), mock[DKnowledge[DataFrame]])
          val fpt = new FitPlusTransform
          a[TooManyPossibleTypesException] shouldBe thrownBy {
            fpt.inferKnowledgeUntyped(inputKnowledge)(mock[InferContext])
          }
        }
        "Estimator's dynamic parameters are invalid" in {
          val estimator = new MockEstimator
          val inputKnowledge: Vector[DKnowledge[DOperable]] =
            Vector(DKnowledge(estimator), mock[DKnowledge[DataFrame]])
          val fpt = new FitPlusTransform
          fpt.setEstimatorParams(JsObject(estimator.paramA.name -> JsNumber(-2)))
          a[DeepLangMultiException] shouldBe thrownBy {
            fpt.inferKnowledgeUntyped(inputKnowledge)(mock[InferContext])
          }
        }
      }
    }
  }
}
