package ai.deepsense.workflowexecutor

import org.apache.spark.api.r._
import org.scalatest.concurrent.TimeLimits
import org.scalatest.mockito.MockitoSugar
import org.scalatest.Matchers
import org.scalatest.PrivateMethodTester
import org.scalatest.WordSpec

import ai.deepsense.workflowexecutor.customcode.CustomCodeEntryPoint

class SparkRBackendSpec extends WordSpec with MockitoSugar with Matchers with TimeLimits with PrivateMethodTester {

  "Spark R Backend" should {
    "return 0 for Entry Point Id" in {
      val sparkRBackend        = new SparkRBackend()
      val customCodeEntryPoint = mock[CustomCodeEntryPoint]
      sparkRBackend.start(customCodeEntryPoint)
      sparkRBackend.entryPointId shouldBe "0"
      sparkRBackend.close()
    }
  }

}
