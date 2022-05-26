package io.deepsense.deeplang.doperations

import org.apache.spark.sql.types.StructType

import io.deepsense.commons.utils.Version
import io.deepsense.deeplang.DOperation.Id
import io.deepsense.deeplang.doperables.dataframe.DataFrame
import io.deepsense.deeplang.doperables.report.Report
import io.deepsense.deeplang.doperables.Estimator
import io.deepsense.deeplang.doperables.Transformer
import io.deepsense.deeplang.inference.InferContext
import io.deepsense.deeplang.inference.InferenceWarnings
import io.deepsense.deeplang.params.NumericParam
import io.deepsense.deeplang.params.Param
import io.deepsense.deeplang.ExecutionContext
import io.deepsense.deeplang.UnitSpec

class EstimatorAsFactorySpec extends UnitSpec {

  import EstimatorAsFactorySpec._

  "EstimatorAsFactory" should {
    "have the same parameters as the Estimator" in {
      val mockEstimator = new MockEstimator
      val mockFactory   = new MockEstimatorFactory
      mockFactory.extractParamMap() shouldBe mockEstimator.extractParamMap()
      mockFactory.params shouldBe mockEstimator.params
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

    val param = NumericParam("b", Some("desc"))

    setDefault(param -> 5)

    override val params: Array[Param[_]] = Array(param)

    override private[deeplang] def _fit(ctx: ExecutionContext, df: DataFrame): Transformer = ???

    override private[deeplang] def _fit_infer(schema: Option[StructType]): Transformer = ???

    override def report: Report = ???

  }

  class MockEstimatorFactory extends EstimatorAsFactory[MockEstimator] {

    override val id: Id = Id.randomId

    override val name: String = "Mock Estimator factory used for tests purposes"

    override val description: String = "Description"

  }

}
