package ai.deepsense.deeplang.actions

import ai.deepsense.commons.utils.Version
import ai.deepsense.deeplang.Action._
import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.actionobjects.report.Report
import ai.deepsense.deeplang.actionobjects.Evaluator
import ai.deepsense.deeplang.actionobjects.MetricValue
import ai.deepsense.deeplang.inference.InferContext
import ai.deepsense.deeplang.inference.InferenceWarnings
import ai.deepsense.deeplang.parameters.NumericParameter
import ai.deepsense.deeplang.parameters.Parameter
import ai.deepsense.deeplang.parameters.ParameterMap
import ai.deepsense.deeplang.Knowledge
import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.ReportTypeDefault
import ai.deepsense.deeplang.UnitSpec

class EvaluatorAsFactorySpec extends UnitSpec {

  import EvaluatorAsFactorySpec._

  "EvaluatorAsFactory" should {
    "have the same parameters as the Evaluator" in {
      val mockEvaluator = new MockEvaluator
      val mockFactory   = new MockEvaluatorFactory
      val reportType    = ReportTypeDefault(mockFactory.reportType)
      mockFactory.extractParamMap() shouldBe mockEvaluator.extractParamMap() ++ ParameterMap(reportType)
      mockFactory.specificParams shouldBe mockEvaluator.params
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

    val param = NumericParameter("b", Some("desc"))

    setDefault(param -> 5)

    override val params: Array[Parameter[_]] = Array(param)

    override private[deeplang] def _evaluate(ctx: ExecutionContext, df: DataFrame): MetricValue =
      ???

    override private[deeplang] def _infer(k: Knowledge[DataFrame]): MetricValue =
      ???

    override def report(extended: Boolean = true): Report =
      ???

    override def isLargerBetter: Boolean = ???

  }

  class MockEvaluatorFactory extends EvaluatorAsFactory[MockEvaluator] {

    override val id: Id = Id.randomId

    override val name: String = "Mock Evaluator factory used for tests purposes"

    override val description: String = "Description"

  }

}
