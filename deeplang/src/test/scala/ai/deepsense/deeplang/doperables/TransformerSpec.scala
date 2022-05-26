package ai.deepsense.deeplang.doperables

import org.mockito.Matchers.any
import org.mockito.Mockito._

import ai.deepsense.deeplang.doperables.dataframe.DataFrame
import ai.deepsense.deeplang.inference.InferContext
import ai.deepsense.deeplang.inference.InferenceWarnings
import ai.deepsense.deeplang.DKnowledge
import ai.deepsense.deeplang.DeeplangTestSupport
import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.UnitSpec

class TransformerSpec extends UnitSpec with DeeplangTestSupport {

  private def transformer = {
    val t = mock[Transformer]
    when(t.transform).thenCallRealMethod()
    when(t._transformSchema(any(), any())).thenCallRealMethod()
    t
  }

  val inputDF = createDataFrame()

  val outputDF = createDataFrame()

  val inputSchema = inputDF.schema.get

  val outputSchema = outputDF.schema.get

  val execCtx = mock[ExecutionContext]

  val inferCtx = mock[InferContext]

  val emptyWarnings = InferenceWarnings.empty

  "Transformer" should {
    "transform DataFrame" in {
      val t = transformer
      when(t._transform(execCtx, inputDF)).thenReturn(outputDF)
      t.transform(execCtx)(())(inputDF) shouldBe outputDF
    }
    "infer schema" when {
      "it's implemented" when {
        val t = transformer
        when(t._transformSchema(inputSchema)).thenReturn(Some(outputSchema))

        val expectedOutputDKnowledge = DKnowledge(DataFrame.forInference(outputSchema))
        "input DKnowledge contains exactly one type" in {
          val inputDKnowledge = DKnowledge(DataFrame.forInference(inputSchema))
          val output          = t.transform.infer(inferCtx)(())(inputDKnowledge)
          output shouldBe (expectedOutputDKnowledge, emptyWarnings)
        }
        "input DKnowledge contains more than one type" in {
          val inputDKnowledge = DKnowledge(
            DataFrame.forInference(inputSchema),
            DataFrame.forInference(inputSchema)
          )
          val output          = t.transform.infer(inferCtx)(())(inputDKnowledge)
          output shouldBe (expectedOutputDKnowledge, emptyWarnings)
        }
      }
    }
    "not infer schema" when {
      "it's not implemented" in {
        val t                        = transformer
        when(t._transformSchema(inputSchema)).thenReturn(None)
        val inputDKnowledge          = DKnowledge(DataFrame.forInference(inputSchema))
        val expectedOutputDKnowledge = DKnowledge(DataFrame.forInference(None))
        val output                   = t.transform.infer(inferCtx)(())(inputDKnowledge)
        output shouldBe (expectedOutputDKnowledge, emptyWarnings)
      }
    }
  }

}
