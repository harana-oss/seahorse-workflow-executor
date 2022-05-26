package ai.deepsense.deeplang.params

import org.scalatestplus.mockito.MockitoSugar
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import spray.json.JsObject
import spray.json.JsValue

import ai.deepsense.models.json.graph.GraphJsonProtocol.GraphReader

abstract class AbstractParamSpec[T, U <: Param[T]] extends AnyWordSpec with Matchers with MockitoSugar {

  def className: String

  def paramFixture: (U, JsValue) // param + its json description

  def valueFixture: (T, JsValue) // value + its json description

  val defaultValue: T = valueFixture._1

  def graphReader: GraphReader = mock[GraphReader]

  def serializeDefaultValue(default: T): JsValue = paramFixture._1.valueToJson(default)

  className should {
    "serialize itself to JSON" when {
      "default value is not provided" in {
        val (param, expectedJson) = paramFixture
        param.toJson(maybeDefault = None) shouldBe expectedJson
      }
      "default value is provided" in {
        val (param, expectedJson)   = paramFixture
        val expectedJsonWithDefault = JsObject(
          expectedJson.asJsObject.fields + ("default" -> serializeDefaultValue(defaultValue))
        )
        param.toJson(maybeDefault = Some(defaultValue)) shouldBe expectedJsonWithDefault
      }
    }
  }

  it should {
    "serialize value to JSON" in {
      val param                 = paramFixture._1
      val (value, expectedJson) = valueFixture
      param.valueToJson(value) shouldBe expectedJson
    }
  }

  it should {
    "deserialize value from JSON" in {
      val param                      = paramFixture._1
      val (expectedValue, valueJson) = valueFixture
      val extractedValue             = param.valueFromJson(valueJson, graphReader)
      extractedValue shouldBe expectedValue
    }
  }

}
