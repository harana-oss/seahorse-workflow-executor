package ai.deepsense.deeplang.params.multivalue

import spray.json._

import ai.deepsense.deeplang.UnitSpec

class MultipleValuesParamJsonSpec extends UnitSpec with BasicFormats {

  "MultipleValuesParam" should {
    "deserialize from json" in {
      val gridParam = MultipleValuesParam.fromJson[Int](json)
      gridParam.values shouldBe Seq(1, 2, 3, 4, 5)
    }
  }

  val json = JsObject(
    "values" -> JsArray(
      JsObject(
        "type"        -> JsString("seq"),
        "value"       -> JsObject("sequence" -> JsArray(JsNumber(1), JsNumber(2.0), JsNumber(3), JsNumber(4)))
      ),
      JsObject("type" -> JsString("seq"), "value" -> JsObject("sequence" -> JsArray(JsNumber(4), JsNumber(5.0))))
    )
  )

}
