package ai.deepsense.deeplang.parameters.choice

import spray.json._

import ai.deepsense.deeplang.parameters.BooleanParameter

sealed trait ChoiceABC extends Choice {

  override val choiceOrder: List[Class[_ <: ChoiceABC]] = List(classOf[OptionB], classOf[OptionC], classOf[OptionA])

}

case class OptionA() extends ChoiceABC {

  override val name = "A"

  val bool = BooleanParameter(name = "bool", description = Some("description"))

  override val params: Array[ai.deepsense.deeplang.parameters.Parameter[_]] = Array(bool)

  def setBool(b: Boolean): this.type = set(bool, b)

}

case class OptionB() extends ChoiceABC {

  override val name = "B"

  override val params: Array[ai.deepsense.deeplang.parameters.Parameter[_]] = Array()

}

case class OptionC() extends ChoiceABC {

  override val name = "C"

  override val params: Array[ai.deepsense.deeplang.parameters.Parameter[_]] = Array()

}

sealed trait BaseChoice extends Choice {

  override val choiceOrder: List[Class[_ <: BaseChoice]] =
    List(classOf[ChoiceWithoutNoArgConstructor])

}

case class ChoiceWithoutNoArgConstructor(x: String) extends BaseChoice {

  override val name: String = "choiceWithoutNoArgConstructor"

  override val params: Array[ai.deepsense.deeplang.parameters.Parameter[_]] = Array()

}

sealed trait ChoiceWithoutDeclaration extends Choice {

  override val choiceOrder: List[Class[_ <: ChoiceWithoutDeclaration]] = List()

}

case class ChoiceWithoutDeclarationInstance() extends ChoiceWithoutDeclaration {

  override val name: String = "choiceWithoutDeclarationInstance"

  override val params: Array[ai.deepsense.deeplang.parameters.Parameter[_]] = Array()

}

object ChoiceFixtures {

  val values = "values" -> JsArray(
    JsObject(
      "name"   -> JsString("B"),
      "schema" -> JsArray()
    ),
    JsObject(
      "name"   -> JsString("C"),
      "schema" -> JsArray()
    ),
    JsObject(
      "name"   -> JsString("A"),
      "schema" -> JsArray(
        JsObject(
          "type"        -> JsString("boolean"),
          "name"        -> JsString("bool"),
          "description" -> JsString("description"),
          "isGriddable" -> JsFalse,
          "default"     -> JsNull
        )
      )
    )
  )

}
