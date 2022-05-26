package io.deepsense.workflowexecutor

import org.apache.spark.api.r._
import org.scalatest.concurrent.TimeLimits
import org.scalatestplus.mockito.MockitoSugar
import org.scalatest.PrivateMethodTester
import io.deepsense.workflowexecutor.customcode.CustomCodeEntryPoint
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class SparkRBackendSpec extends AnyWordSpec with MockitoSugar with Matchers with TimeLimits with PrivateMethodTester {

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
