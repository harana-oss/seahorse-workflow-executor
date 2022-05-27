package ai.deepsense.deeplang.parameters.choice

import scala.reflect.runtime.universe._

import spray.json.DeserializationException
import spray.json.JsObject

import ai.deepsense.deeplang.parameters.exceptions.NoArgumentConstructorRequiredException
import ai.deepsense.deeplang.parameters.AbstractParameterSpec
import ai.deepsense.deeplang.parameters.Parameter
import ai.deepsense.models.json.graph.GraphJsonProtocol.GraphReader

abstract class AbstractChoiceParamSpec[T, U <: Parameter[T]] extends AbstractParameterSpec[T, U] {

  protected def createChoiceParam[V <: Choice: TypeTag](name: String, description: String): Parameter[V]

  className should {
    "throw an exception when choices don't have no-arg constructor" in {
      a[NoArgumentConstructorRequiredException] should be thrownBy
        createChoiceParam[BaseChoice]("name", "description")
    }
    "throw an exception when unsupported choice is given" in {
      val graphReader = mock[GraphReader]
      a[DeserializationException] should be thrownBy
        createChoiceParam[ChoiceABC]("name", "description").valueFromJson(
          JsObject(
            "unsupportedClass" -> JsObject()
          ),
          graphReader
        )
    }
    "throw an exception when not all choices are declared" in {
      an[IllegalArgumentException] should be thrownBy
        createChoiceParam[ChoiceWithoutDeclaration]("name", "description")
    }
  }

}
