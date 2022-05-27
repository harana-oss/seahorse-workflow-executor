package ai.deepsense.models.json.workflow

import scala.collection.immutable.SortedMap
import scala.collection.immutable.ListMap

import org.scalatest.flatspec.AnyFlatSpec
import org.mockito.Mockito._
import org.scalatestplus.mockito.MockitoSugar
import org.scalatest.matchers.should.Matchers
import org.scalatest.matchers.should.Matchers
import spray.json._
import ai.deepsense.deeplang.Action
import ai.deepsense.deeplang.catalogs.SortPriority
import ai.deepsense.deeplang.catalogs.actions.ActionCategory
import ai.deepsense.deeplang.catalogs.actions.ActionCategoryNode
import ai.deepsense.deeplang.catalogs.actions.ActionDescriptor
import ai.deepsense.models.json.workflow.ActionCategoryNodeJsonProtocol._

object SortPriorityTest

class ActionCategoryNodeJsonProtocolSpec extends AnyFlatSpec with Matchers with MockitoSugar {

  "ActionCategoryNode" should "be correctly serialized to json" in {
    val childCategory =
      new ActionCategory(ActionCategory.Id.randomId, "mock child name", SortPriority.coreDefault) {}
    val childNode     = ActionCategoryNode(Some(childCategory))

    val operationDescriptor = mock[ActionDescriptor]
    when(operationDescriptor.id).thenReturn(Action.Id.randomId)
    when(operationDescriptor.name).thenReturn("mock operation descriptor name")
    when(operationDescriptor.description).thenReturn("mock operator descriptor description")

    val node = ActionCategoryNode(
      None,
      successors = SortedMap(childCategory -> childNode),
      operations = List(operationDescriptor)
    )

    val expectedJson = JsObject(
      "catalog" -> JsArray(
        JsObject(
          "id"      -> JsString(childCategory.id.toString),
          "name"    -> JsString(childCategory.name),
          "catalog" -> JsArray(),
          "items"   -> JsArray()
        )
      ),
      "items"   -> JsArray(
        JsObject(
          "id"          -> JsString(operationDescriptor.id.toString),
          "name"        -> JsString(operationDescriptor.name),
          "description" -> JsString(operationDescriptor.description)
        )
      )
    )

    node.toJson shouldBe expectedJson
  }

}
