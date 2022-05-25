package io.deepsense.deeplang.doperations

import io.deepsense.commons.utils.Version
import io.deepsense.deeplang.DOperation._
import io.deepsense.deeplang.doperables.dataframe.DataFrame
import io.deepsense.deeplang.doperables.report.Report
import io.deepsense.deeplang.doperables.{Evaluator, MetricValue}
import io.deepsense.deeplang.inference.{InferContext, InferenceWarnings}
import io.deepsense.deeplang.params.{NumericParam, Param}
import io.deepsense.deeplang.{DKnowledge, ExecutionContext, UnitSpec}

class EvaluatorAsFactorySpec extends UnitSpec {
  import EvaluatorAsFactorySpec._

  "EvaluatorAsFactory" should {
    "have the same parameters as the Evaluator" in {
      val mockEvaluator = new MockEvaluator
      val mockFactory = new MockEvaluatorFactory
      mockFactory.extractParamMap() shouldBe mockEvaluator.extractParamMap()
      mockFactory.params shouldBe mockEvaluator.params
    }

    val paramValue1 = 100
    val paramValue2 = 1337

    "produce an Evaluator with parameters set" in {
      val mockFactory = new MockEvaluatorFactory
      mockFactory.set(mockFactory.evaluator.param -> paramValue1)
      val Vector(evaluator: MockEvaluator) =
        mockFactory.executeUntyped(Vector.empty)(mock[ExecutionContext])

      evaluator.get(mockFactory.evaluator.param) shouldBe Some(paramValue1)
    }

    "propagate parameters to wrapped evaluator" in {
      val mockFactory = new MockEvaluatorFactory
      mockFactory.set(mockFactory.evaluator.param -> paramValue1)
      val evaluator1 = execute(mockFactory)
      evaluator1.get(mockFactory.evaluator.param) shouldBe Some(paramValue1)

      mockFactory.set(mockFactory.evaluator.param -> paramValue2)
      val evaluator2 = execute(mockFactory)
      evaluator2.get(mockFactory.evaluator.param) shouldBe Some(paramValue2)
    }

    "infer knowledge" in {
      val mockFactory = new MockEvaluatorFactory
      mockFactory.set(mockFactory.evaluator.param -> paramValue1)

      val (Vector(knowledge), warnings) = mockFactory.inferKnowledgeUntyped(Vector.empty)(mock[InferContext])

      knowledge should have size 1
      knowledge.single shouldBe a[MockEvaluator]
      val evaluator = knowledge.single.asInstanceOf[MockEvaluator]
      evaluator.extractParamMap() shouldBe execute(mockFactory).extractParamMap()

      warnings shouldBe InferenceWarnings.empty
    }
  }

  private def execute(factory: MockEvaluatorFactory): MockEvaluator =
    factory.executeUntyped(Vector.empty)(mock[ExecutionContext]).head.asInstanceOf[MockEvaluator]
}

object EvaluatorAsFactorySpec {

  class MockEvaluator extends Evaluator {
    val param = NumericParam("b", Some("desc"))
    setDefault(param -> 5)
    override val params: Array[Param[_]] = Array(param)

    override private[deeplang] def _evaluate(ctx: ExecutionContext, df: DataFrame): MetricValue =
      ???
    override private[deeplang] def _infer(k: DKnowledge[DataFrame]): MetricValue =
      ???
    override def report: Report =
      ???

    override def isLargerBetter: Boolean = ???
  }

  class MockEvaluatorFactory extends EvaluatorAsFactory[MockEvaluator] {
    override val id: Id = Id.randomId
    override val name: String = "Mock Evaluator factory used for tests purposes"
    override val description: String = "Description"
  }
}
