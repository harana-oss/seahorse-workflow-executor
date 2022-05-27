package ai.deepsense.deeplang.actionobjects

import org.mockito.Mockito._

import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.inference.InferContext
import ai.deepsense.deeplang.inference.InferenceWarnings
import ai.deepsense.deeplang.Knowledge
import ai.deepsense.deeplang.DeeplangTestSupport
import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.UnitSpec

class EstimatorIntegSpec extends UnitSpec with DeeplangTestSupport {

  private def estimator = {
    val e = mock[Estimator[Transformer]]
    when(e.fit).thenCallRealMethod()
    e
  }

  val transformer = mock[Transformer]

  "Estimator" should {
    "fit to a DataFrame producing a Transfomer" in {
      val dataFrame: DataFrame      = mock[DataFrame]
      val e                         = estimator
      val context: ExecutionContext = mock[ExecutionContext]
      when(e._fit(context, dataFrame)).thenReturn(transformer)
      val outputTransfomer          = e.fit(context)(())(dataFrame)
      outputTransfomer shouldBe transformer
    }
    "infer" when {
      "input DKnowledge contains exactly one type" in {
        val schema                      = createSchema()
        val inputDKnowledge             = Knowledge(
          DataFrame.forInference(schema)
        )
        val e                           = estimator
        when(e._fit_infer(Some(schema))).thenReturn(transformer)
        val (outputKnowledge, warnings) = e.fit.infer(mock[InferContext])(())(inputDKnowledge)
        outputKnowledge shouldBe Knowledge(transformer)
        warnings shouldBe InferenceWarnings.empty
      }
      "input DKnowledge contains more than one type (by taking the first type)" in {
        val schema                      = createSchema()
        val schema2                     = createSchema()
        val inputDKnowledge             = Knowledge(
          DataFrame.forInference(schema),
          DataFrame.forInference(schema2)
        )
        val e                           = estimator
        when(e._fit_infer(Some(schema))).thenReturn(transformer)
        val (outputKnowledge, warnings) = e.fit.infer(mock[InferContext])(())(inputDKnowledge)
        outputKnowledge shouldBe Knowledge(transformer)
        warnings shouldBe InferenceWarnings.empty
      }
    }
  }

}
