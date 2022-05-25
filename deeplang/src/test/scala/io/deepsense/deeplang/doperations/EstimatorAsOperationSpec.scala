package io.deepsense.deeplang.doperations

import org.apache.spark.sql.types.StructType

import io.deepsense.commons.utils.Version
import io.deepsense.deeplang.DOperation.Id
import io.deepsense.deeplang._
import io.deepsense.deeplang.doperables._
import io.deepsense.deeplang.doperables.dataframe.DataFrame
import io.deepsense.deeplang.doperations.MockDOperablesFactory._
import io.deepsense.deeplang.inference.{InferContext, InferenceWarnings}
import io.deepsense.deeplang.params.ParamMap

class EstimatorAsOperationSpec extends UnitSpec with DeeplangTestSupport {
  import EstimatorAsOperationSpec._

  "EstimatorAsOperation" should {
    def createMockOperation: MockEstimatorOperation = new MockEstimatorOperation
    "have the same parameters as the Estimator" in {
      val op = createMockOperation
      op.params shouldBe op.estimator.params
    }
   "have the same default values for parameters as Estimator" in {
     val op = createMockOperation
     op.extractParamMap() shouldBe ParamMap(createMockOperation.estimator.paramA -> DefaultForA)
    }
    "execute fit using properly set parameters" in {
      def testFit(
          op: MockEstimatorOperation,
          expectedDataFrame: DataFrame,
          expectedTransformer: Transformer): Unit = {
        val Vector(outputDataFrame: DataFrame, outputTransformer: Transformer) =
          op.executeUntyped(Vector(mock[DataFrame]))(mock[ExecutionContext])
        outputDataFrame shouldBe expectedDataFrame
        outputTransformer shouldBe expectedTransformer
      }
      val op = createMockOperation
      testFit(op, transformedDataFrame1, transformer1)
      op.set(op.estimator.paramA -> 2)
      testFit(op, transformedDataFrame2, transformer2)
    }
    "infer types using properly set parameters" in {
      def testInference(
          op: MockEstimatorOperation,
          expectedSchema: StructType,
          expectedTransformerKnowledge: DKnowledge[Transformer]): Unit = {

        val inputDF = DataFrame.forInference(createSchema())
        val (knowledge, warnings) = op.inferKnowledgeUntyped(Vector(DKnowledge(inputDF)))(mock[InferContext])
        // Warnings should be a sum of transformer inference warnings
        // and estimator inference warnings. Currently, either both of them
        // are empty or the inferences throw exception, so the sum is always 'empty'.
        warnings shouldBe InferenceWarnings.empty
        val Vector(dataFrameKnowledge, transformerKnowledge) = knowledge
        dataFrameKnowledge shouldBe DKnowledge(DataFrame.forInference(expectedSchema))
        transformerKnowledge shouldBe expectedTransformerKnowledge
      }

      val op = createMockOperation
      testInference(op, transformedDataFrameSchema1, transformerKnowledge1)
      op.set(op.estimator.paramA -> 2)
      testInference(op, transformedDataFrameSchema2, transformerKnowledge2)
    }
  }
}

object EstimatorAsOperationSpec extends UnitSpec {
  class MockEstimatorOperation
    extends EstimatorAsOperation[MockEstimator, Transformer] {
    override val id: Id = Id.randomId
    override val name: String = "Mock Estimator as an Operation"
    override val description: String = "Description"
  }
}

