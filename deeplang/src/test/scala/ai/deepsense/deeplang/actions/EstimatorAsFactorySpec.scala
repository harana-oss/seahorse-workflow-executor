package ai.deepsense.deeplang.actions

import org.apache.spark.sql.types.StructType
import ai.deepsense.commons.utils.Version
import ai.deepsense.deeplang.Action.Id
import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.deeplang.actionobjects.report.Report
import ai.deepsense.deeplang.actionobjects.Estimator
import ai.deepsense.deeplang.actionobjects.Transformer
import ai.deepsense.deeplang.inference.InferContext
import ai.deepsense.deeplang.inference.InferenceWarnings
import ai.deepsense.deeplang.parameters.NumericParameter
import ai.deepsense.deeplang.parameters.Parameter
import ai.deepsense.deeplang.parameters.ParameterMap
import ai.deepsense.deeplang.Action
import ai.deepsense.deeplang.ExecutionContext
import ai.deepsense.deeplang.ReportTypeDefault
import ai.deepsense.deeplang.UnitSpec

class EstimatorAsFactorySpec extends UnitSpec {

  import EstimatorAsFactorySpec._

  "EstimatorAsFactory" should {
    "have the same parameters as the Estimator" in {
      val mockEstimator      = new MockEstimator
      val mockFactory        = new MockEstimatorFactory
      val reportTypeParamMap = ParameterMap(ReportTypeDefault(mockFactory.reportType))
      mockFactory.extractParamMap() shouldBe mockEstimator.extractParamMap() ++ reportTypeParamMap
      mockFactory.specificParams shouldBe mockEstimator.params
    }
    val paramValue1 = 100
    val paramValue2 = 1337
    "produce an Estimator with parameters set" in {
      val mockFactory = new MockEstimatorFactory
      mockFactory.set(mockFactory.estimator.param -> paramValue1)
      val Vector(estimator: MockEstimator) =
        mockFactory.executeUntyped(Vector.empty)(mock[ExecutionContext])

      estimator.get(mockFactory.estimator.param) shouldBe Some(paramValue1)
    }
    "return the same instance of estimator each time" in {
      val mockFactory = new MockEstimatorFactory
      mockFactory.set(mockFactory.estimator.param -> paramValue1)
      val estimator1 = execute(mockFactory)
      estimator1.get(mockFactory.estimator.param) shouldBe Some(paramValue1)

      mockFactory.set(mockFactory.estimator.param -> paramValue2)
      val estimator2 = execute(mockFactory)
      estimator2.get(mockFactory.estimator.param) shouldBe Some(paramValue2)

    }
    "infer knowledge" in {
      val mockFactory = new MockEstimatorFactory
      mockFactory.set(mockFactory.estimator.param -> paramValue1)

      val (Vector(knowledge), warnings) = mockFactory.inferKnowledgeUntyped(Vector.empty)(mock[InferContext])

      knowledge should have size 1
      knowledge.single shouldBe a[MockEstimator]
      val estimator = knowledge.single.asInstanceOf[MockEstimator]
      estimator.extractParamMap() shouldBe execute(mockFactory).extractParamMap()

      warnings shouldBe InferenceWarnings.empty
    }
  }

  private def execute(factory: MockEstimatorFactory): MockEstimator =
    factory.executeUntyped(Vector.empty)(mock[ExecutionContext]).head.asInstanceOf[MockEstimator]

}

object EstimatorAsFactorySpec {

  class MockEstimator extends Estimator[Transformer] {

    val param = NumericParameter("b", Some("desc"))

    setDefault(param -> 5)

    override val params: Array[Parameter[_]] = Array(param)

    override private[deeplang] def _fit(ctx: ExecutionContext, df: DataFrame): Transformer = ???

    override private[deeplang] def _fit_infer(schema: Option[StructType]): Transformer = ???

    override def report(extended: Boolean = true): Report = ???

  }

  class MockEstimatorFactory extends EstimatorAsFactory[MockEstimator] {

    override val id: Id = Id.randomId

    override val name: String = "Mock Estimator factory used for tests purposes"

    override val description: String = "Description"

  }

}
