package io.deepsense.models.json.workflow

import scala.reflect.runtime.universe.{TypeTag, typeOf}

import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{FlatSpec, Matchers}
import spray.json._

import io.deepsense.deeplang.{DPortPosition, DOperation}
import io.deepsense.deeplang.catalogs.doperations.{DOperationCategory, DOperationDescriptor}
import io.deepsense.deeplang.params.Params

object HelperTypes {
  class A
  class B
  trait T1
  trait T2
}

class DOperationDescriptorJsonProtocolSpec
  extends FlatSpec
  with MockitoSugar
  with Matchers
  with DOperationDescriptorJsonProtocol {

  "DOperationDescriptor" should "be correctly serialized to json" in {
    val (operationDescriptor, expectedJson) = operationDescriptorWithExpectedJsRepresentation
    operationDescriptor.toJson(DOperationDescriptorFullFormat) shouldBe expectedJson
  }

  it should "be correctly serialized to json omitting its parameters" in {
    val (operationDescriptor, expectedJson) = operationDescriptorWithExpectedJsRepresentation
    val jsonWithoutParameters = JsObject(expectedJson.asJsObject.fields - "parameters")
    operationDescriptor.toJson(DOperationDescriptorBaseFormat) shouldBe jsonWithoutParameters
  }

  private[this] def operationDescriptorWithExpectedJsRepresentation:
  (DOperationDescriptor, JsValue) = {

    import io.deepsense.models.json.workflow.HelperTypes._

    val category = mock[DOperationCategory]
    when(category.id) thenReturn DOperationCategory.Id.randomId

    val parameters = mock[Params]
    val parametersJsRepresentation = JsString("Mock parameters representation")
    when(parameters.paramsToJson) thenReturn parametersJsRepresentation

    val operationDescriptor = DOperationDescriptor(
      DOperation.Id.randomId,
      "operation name",
      "operation description",
      category,
      hasDocumentation = false,
      parameters.paramsToJson,
      Seq(typeOf[A], typeOf[A with T1]),
      Vector(DPortPosition.Left, DPortPosition.Center),
      Seq(typeOf[B], typeOf[B with T2]),
      Vector(DPortPosition.Right, DPortPosition.Center))

    def name[T: TypeTag]: String = typeOf[T].typeSymbol.fullName

    val expectedJson = JsObject(
      "id" -> JsString(operationDescriptor.id.toString),
      "name" -> JsString(operationDescriptor.name),
      "category" -> JsString(category.id.toString),
      "description" -> JsString(operationDescriptor.description),
      "deterministic" -> JsBoolean(false),
      "hasDocumentation" -> JsBoolean(false),
      "parameters" -> parametersJsRepresentation,
      "ports" -> JsObject(
        "input" -> JsArray(
          JsObject(
            "portIndex" -> JsNumber(0),
            "required" -> JsBoolean(true),
            "typeQualifier" -> JsArray(JsString(name[A])),
            "portPosition" -> JsString("left")
          ),
          JsObject(
            "portIndex" -> JsNumber(1),
            "required" -> JsBoolean(true),
            "typeQualifier" -> JsArray(JsString(name[A]), JsString(name[T1])),
            "portPosition" -> JsString("center")
          )
        ),
        "output" -> JsArray(
          JsObject(
            "portIndex" -> JsNumber(0),
            "typeQualifier" -> JsArray(JsString(name[B])),
            "portPosition" -> JsString("right")
          ),
          JsObject(
            "portIndex" -> JsNumber(1),
            "typeQualifier" -> JsArray(JsString(name[B]), JsString(name[T2])),
            "portPosition" -> JsString("center")
          )
        )
      )
    )

    (operationDescriptor, expectedJson)
  }
}
