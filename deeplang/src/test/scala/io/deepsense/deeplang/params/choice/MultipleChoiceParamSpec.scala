package io.deepsense.deeplang.params.choice

import scala.reflect.runtime.universe._

import spray.json._

class MultipleChoiceParamSpec extends AbstractChoiceParamSpec[Set[ChoiceABC], MultipleChoiceParam[ChoiceABC]] {

  override def className: String = "MultipleChoiceParam"

  className should {
    "serialize default values properly" in {
      val allChoicesSelection: Set[ChoiceABC] = Set(OptionA(), OptionB(), OptionC())
      val expected                            = JsArray(JsString("A"), JsString("B"), JsString("C"))
      val serializedArray                     = serializeDefaultValue(allChoicesSelection).asInstanceOf[JsArray]
      serializedArray.elements should contain theSameElementsAs expected.elements
    }
  }

  override def paramFixture: (MultipleChoiceParam[ChoiceABC], JsValue) = {
    val description         = "description"
    val multipleChoiceParam = MultipleChoiceParam[ChoiceABC]("name", Some(description))
    val multipleChoiceExpectedJson = JsObject(
      "type"        -> JsString("multipleChoice"),
      "name"        -> JsString(multipleChoiceParam.name),
      "description" -> JsString(description),
      "isGriddable" -> JsFalse,
      "default"     -> JsNull,
      ChoiceFixtures.values
    )
    (multipleChoiceParam, multipleChoiceExpectedJson)
  }

  override def valueFixture: (Set[ChoiceABC], JsValue) = {
    val choices = Set[ChoiceABC](
      OptionA().setBool(true),
      OptionC()
    )
    val expectedJson = JsObject(
      "A" -> JsObject(
        "bool" -> JsTrue
      ),
      "C" -> JsObject()
    )
    (choices, expectedJson)
  }

  override def serializeDefaultValue(default: Set[ChoiceABC]): JsValue =
    JsArray(default.toSeq.map(_.name).map(JsString(_)): _*)

  override protected def createChoiceParam[V <: Choice: TypeTag](name: String, description: String): ChoiceParam[V] =
    ChoiceParam[V](name, Some(description))

}
