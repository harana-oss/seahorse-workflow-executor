package ai.deepsense.deeplang.actions

import scala.collection.mutable
import scala.reflect.runtime.universe.TypeTag

import org.apache.spark.sql.types.StructType
import ai.deepsense.commons.utils.Version
import ai.deepsense.deeplang.Action.Id
import ai.deepsense.deeplang.actionobjects.Transformer
import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.actionobjects.report.Report
import ai.deepsense.deeplang.inference.InferContext
import ai.deepsense.deeplang.inference.InferenceWarnings
import ai.deepsense.deeplang.parameters.validators.RangeValidator
import ai.deepsense.deeplang.parameters.NumericParameter
import ai.deepsense.deeplang.parameters.Parameter
import ai.deepsense.deeplang.parameters.ParameterMap
import ai.deepsense.deeplang.parameters.ParamPair
import ai.deepsense.deeplang._

object MockTransformers extends UnitSpec with DeeplangTestSupport {

  val DefaultForA = 1

  val inputDataFrame = createDataFrame()

  val outputDataFrame1 = createDataFrame()

  val outputDataFrame2 = createDataFrame()

  val inputSchema = inputDataFrame.schema.get

  val outputSchema1 = outputDataFrame1.schema.get

  val outputSchema2 = outputDataFrame2.schema.get

  class MockTransformer extends Transformer {

    val paramA = NumericParameter("a", Some("desc"), RangeValidator(0.0, Double.MaxValue))

    setDefault(paramA -> DefaultForA)

    override val params: Array[Parameter[_]] = Array(paramA)

    override def report(extended: Boolean = true): Report = ???

    override protected def applyTransform(ctx: ExecutionContext, df: DataFrame): DataFrame = {
      $(paramA) match {
        case 1      => outputDataFrame1
        case -2 | 2 => outputDataFrame2
      }
    }

    override protected def applyTransformSchema(schema: StructType): Option[StructType] = {
      Some($(paramA) match {
        case 1      => outputSchema1
        case -2 | 2 => outputSchema2
      })
    }

    override def load(ctx: ExecutionContext, path: String): this.type = ???

    override protected def saveTransformer(ctx: ExecutionContext, path: String): Unit = ???

  }

}

class TransformerAsOperationSpec extends UnitSpec {

  import MockTransformers._

  class MockTransformerAsOperation extends TransformerAsOperation[MockTransformer] {

    override val tTagTO_1: TypeTag[MockTransformer] = typeTag[MockTransformer]

    override val name: String = ""

    override val id: Id = "6d924962-9456-11e5-8994-feff819cdc9f"

    override val description: String = ""

  }

  "TransformerAsOperation" should {
    def operation: MockTransformerAsOperation = new MockTransformerAsOperation
    val op                                    = operation
    val changedParamMap                       =
      ParameterMap(op.transformer.paramA -> 2, op.reportType -> Action.ReportParam.Extended())
    "have specific params same as Transformer" in {
      op.specificParams shouldBe Array(op.transformer.paramA)
    }

    "have report type param set to extended" in {
      op.extractParamMap().get(op.reportType).get shouldBe Action.ReportParam.Extended()
    }

    "have defaults same as in Transformer" in {
      op.extractParamMap() shouldBe ParameterMap(op.transformer.paramA -> DefaultForA, ReportTypeDefault(op.reportType))
    }
    "execute transform using transformer with properly set params and return it" in {
      op.set(op.transformer.paramA -> 2)
      val result = op.executeUntyped(Vector(mock[DataFrame]))(mock[ExecutionContext])

      (result should have).length(2)
      result(0).asInstanceOf[DataFrame] shouldBe outputDataFrame2
      result(1).asInstanceOf[MockTransformer].extractParamMap() shouldBe changedParamMap

    }
    "infer types on transformer with properly set params and return it" in {
      op.set(op.transformer.paramA -> 2)

      val inputDF            = inputDataFrame
      val (result, warnings) =
        op.inferKnowledgeUntyped(Vector(Knowledge(inputDF)))(mock[InferContext])

      warnings shouldBe InferenceWarnings.empty

      (result should have).length(2)
      result(0).asInstanceOf[Knowledge[DataFrame]] shouldBe
        Knowledge(DataFrame.forInference(outputSchema2))
      result(1).single.asInstanceOf[MockTransformer].extractParamMap() shouldBe changedParamMap
    }
  }

}
