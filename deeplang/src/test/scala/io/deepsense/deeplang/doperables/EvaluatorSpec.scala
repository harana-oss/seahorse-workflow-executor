package io.deepsense.deeplang.doperables

import org.mockito.Mockito._

import io.deepsense.deeplang.doperables.dataframe.DataFrame
import io.deepsense.deeplang.inference.{InferContext, InferenceWarnings}
import io.deepsense.deeplang.{DKnowledge, ExecutionContext, UnitSpec}

class EvaluatorSpec extends UnitSpec {

  private def evaluator = {
    val e = mock[Evaluator]
    when(e.evaluate) thenCallRealMethod()
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

      when(e._evaluate(execCtx, dataFrame)) thenReturn metricValue
      e.evaluate(execCtx)(())(dataFrame) shouldBe metricValue
    }

    "infer knowledge" in {
      val e = evaluator
      when(e._infer(DKnowledge(dataFrame))) thenReturn metricValue

      val (knowledge, warnings) = e.evaluate.infer(inferCtx)(())(DKnowledge(dataFrame))
      knowledge.single shouldBe metricValue
      warnings shouldBe emptyWarnings
    }
  }
}
