package io.deepsense.deeplang.params

import org.scalatest.mockito.MockitoSugar
import org.scalatest.{Matchers, WordSpec}
import spray.json.{JsObject, JsValue}

abstract class AbstractParamSpec[T, U <: Param[T]]
  extends WordSpec
  with Matchers
  with MockitoSugar {

  def className: String

  def paramFixture: (U, JsValue)  // param + its json description

  def valueFixture: (T, JsValue)  // value + its json description

  val defaultValue: T = valueFixture._1

  def serializeDefaultValue(default: T): JsValue = paramFixture._1.valueToJson(default)

  className should {
    "serialize itself to JSON" when {
      "default value is not provided" in {
        val (param, expectedJson) = paramFixture
        param.toJson(maybeDefault = None) shouldBe expectedJson
      }
      "default value is provided" in {
        val (param, expectedJson) = paramFixture
        val expectedJsonWithDefault = JsObject(
          expectedJson.asJsObject.fields + ("default" -> serializeDefaultValue(defaultValue))
        )
        param.toJson(maybeDefault = Some(defaultValue)) shouldBe expectedJsonWithDefault
      }
    }
  }

  it should {
    "serialize value to JSON" in {
      val param = paramFixture._1
      val (value, expectedJson) = valueFixture
      param.valueToJson(value) shouldBe expectedJson
    }
  }

  it should {
    "deserialize value from JSON" in {
      val param = paramFixture._1
      val (expectedValue, valueJson) = valueFixture
      val extractedValue = param.valueFromJson(valueJson)
      extractedValue shouldBe expectedValue
    }
  }
}
