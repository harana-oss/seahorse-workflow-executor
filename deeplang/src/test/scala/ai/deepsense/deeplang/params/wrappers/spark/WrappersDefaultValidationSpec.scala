package ai.deepsense.deeplang.params.wrappers.spark

import org.apache.spark.ml
import org.apache.spark.ml.param._
import org.scalatestplus.mockito.MockitoSugar
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class WrappersDefaultValidationSpec extends AnyWordSpec with Matchers with MockitoSugar {

  class ExampleSparkParams extends ml.param.Params {

    override val uid: String = "id"

    val intSparkParam = new IntParam("", "name", "description")

    val floatSparkParam = new FloatParam("", "name", "description")

    val doubleSparkParam = new DoubleParam("", "name", "description")

    override def copy(extra: ParamMap): Params = ???

  }

  "IntParamWrapper" should {

    val intParamWrapper = new IntParamWrapper[ExampleSparkParams]("name", Some("description"), _.intSparkParam)

    "validate whole Int range" in {
      intParamWrapper.validate(Int.MinValue + 1) shouldBe empty
      intParamWrapper.validate(Int.MaxValue - 1) shouldBe empty
    }
    "reject fractional values" in {
      intParamWrapper.validate(Int.MinValue + 0.005) should have size 1
      intParamWrapper.validate(Int.MaxValue - 0.005) should have size 1
    }
  }

  "FloatParamWrapper" should {

    val floatParamWrapper = new FloatParamWrapper[ExampleSparkParams]("name", Some("description"), _.floatSparkParam)

    "validate whole Float range" in {
      floatParamWrapper.validate(Float.MinValue + 1) shouldBe empty
      floatParamWrapper.validate(Float.MaxValue - 1) shouldBe empty
    }
    "reject values out of Float range" in {
      floatParamWrapper.validate(Double.MinValue + 1) should have size 1
      floatParamWrapper.validate(Double.MaxValue - 1) should have size 1
    }
  }

  "DoubleParamWrapper" should {
    "validate whole Double range" in {
      val doubleParamWrapper =
        new DoubleParamWrapper[ExampleSparkParams]("name", Some("description"), _.doubleSparkParam)
      doubleParamWrapper.validate(Double.MinValue + 1) shouldBe empty
      doubleParamWrapper.validate(Double.MinValue - 1) shouldBe empty
    }
  }

}
