package ai.deepsense.deeplang.actionobjects

import org.mockito.Mockito._

import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.inference.InferContext
import ai.deepsense.deeplang.inference.InferenceWarnings
import ai.deepsense.deeplang.Knowledge
import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.UnitSpec

class EvaluatorSpec extends UnitSpec {

  private def evaluator = {
    val e = mock[Evaluator]
    when(e.evaluate).thenCallRealMethod()
    e
  }

  val dataFrame = mock[DataFrame]

  val metricValue = mock[MetricValue]

  val execCtx = mock[ExecutionContext]

  val inferCtx = mock[InferContext]

  val emptyWarnings = InferenceWarnings.empty

  "Evaluator" should {

    "evaluate DataFrame" in {
      val e = evaluator

      when(e._evaluate(execCtx, dataFrame)).thenReturn(metricValue)
      e.evaluate(execCtx)(())(dataFrame) shouldBe metricValue
    }

    "infer knowledge" in {
      val e = evaluator
      when(e._infer(Knowledge(dataFrame))).thenReturn(metricValue)

      val (knowledge, warnings) = e.evaluate.infer(inferCtx)(())(Knowledge(dataFrame))
      knowledge.single shouldBe metricValue
      warnings shouldBe emptyWarnings
    }
  }

}
