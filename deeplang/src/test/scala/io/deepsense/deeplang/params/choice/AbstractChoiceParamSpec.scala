package io.deepsense.deeplang.params.choice

import scala.reflect.runtime.universe._

import spray.json.{DeserializationException, JsObject}

import io.deepsense.deeplang.params.exceptions.NoArgumentConstructorRequiredException
import io.deepsense.deeplang.params.{AbstractParamSpec, Param}

abstract class AbstractChoiceParamSpec[T, U <: Param[T]] extends AbstractParamSpec[T, U] {

  protected def createChoiceParam[V <: Choice : TypeTag](
    name: String, description: String): Param[V]

  className should {
    "throw an exception when choices don't have no-arg constructor" in {
      a[NoArgumentConstructorRequiredException] should be thrownBy
        createChoiceParam[BaseChoice]("name", "description")
    }
    "throw an exception when unsupported choice is given" in {
      a[DeserializationException] should be thrownBy
        createChoiceParam[ChoiceABC]("name", "description").valueFromJson(
          JsObject(
            "unsupportedClass" -> JsObject()
          )
        )
    }
    "throw an exception when not all choices are declared" in {
      an[IllegalArgumentException] should be thrownBy
        createChoiceParam[ChoiceWithoutDeclaration]("name", "description")
    }
  }
}
