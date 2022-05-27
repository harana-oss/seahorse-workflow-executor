package ai.deepsense.deeplang.actions

import org.apache.spark.sql.types.StructType
import ai.deepsense.commons.utils.Version
import ai.deepsense.deeplang.Action.Id
import ai.deepsense.deeplang._
import ai.deepsense.deeplang.actionobjects._
import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.actions.MockActionObjectsFactory._
import ai.deepsense.deeplang.inference.InferContext
import ai.deepsense.deeplang.inference.InferenceWarnings
import ai.deepsense.deeplang.parameters.ParameterMap

class EstimatorAsOperationSpec extends UnitSpec with DeeplangTestSupport {

  import EstimatorAsOperationSpec._

  "EstimatorAsOperation" should {
    def createMockOperation: MockEstimatorOperation = new MockEstimatorOperation
    "have the same specific parameters as the Estimator" in {
      val op = createMockOperation
      op.specificParams shouldBe op.estimator.params
    }
    "have the same default values for parameters as Estimator" in {
      val op                 = createMockOperation
      val estimatorOperation = op.estimator.paramA -> DefaultForA
      op.extractParamMap() shouldBe ParameterMap(estimatorOperation, ReportTypeDefault(op.reportType))
    }
    "execute fit using properly set parameters" in {
      def testFit(op: MockEstimatorOperation, expectedDataFrame: DataFrame, expectedTransformer: Transformer): Unit = {
        val Vector(outputDataFrame: DataFrame, outputTransformer: Transformer) =
          op.executeUntyped(Vector(mock[DataFrame]))(mock[ExecutionContext])
        outputDataFrame shouldBe expectedDataFrame
        outputTransformer shouldBe expectedTransformer
      }
      val op                                                                                                        = createMockOperation
      testFit(op, transformedDataFrame1, transformer1)
      op.set(op.estimator.paramA -> 2)
      testFit(op, transformedDataFrame2, transformer2)
    }
    "infer types using properly set parameters" in {
      def testInference(
          op: MockEstimatorOperation,
          expectedSchema: StructType,
          expectedTransformerKnowledge: Knowledge[Transformer]
      ): Unit = {

        val inputDF                                          = DataFrame.forInference(createSchema())
        val (knowledge, warnings)                            = op.inferKnowledgeUntyped(Vector(Knowledge(inputDF)))(mock[InferContext])
        // Warnings should be a sum of transformer inference warnings
        // and estimator inference warnings. Currently, either both of them
        // are empty or the inferences throw exception, so the sum is always 'empty'.
        warnings shouldBe InferenceWarnings.empty
        val Vector(dataFrameKnowledge, transformerKnowledge) = knowledge
        dataFrameKnowledge shouldBe Knowledge(DataFrame.forInference(expectedSchema))
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

  class MockEstimatorOperation extends EstimatorAsOperation[MockEstimator, Transformer] {

    override val id: Id = Id.randomId

    override val name: String = "Mock Estimator as an Operation"

    override val description: String = "Description"

  }

}
