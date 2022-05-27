package ai.deepsense.models.json.workflow

import scala.reflect.runtime.universe.Type

import spray.httpx.SprayJsonSupport
import spray.json._

import ai.deepsense.commons.json.EnumerationSerializer
import ai.deepsense.commons.json.IdJsonProtocol
import ai.deepsense.deeplang.PortPosition._
import ai.deepsense.deeplang.catalogs.actions.ActionDescriptor
import ai.deepsense.deeplang.PortPosition
import ai.deepsense.deeplang.utils.TypeUtils

/** Exposes various json formats of ActionDescription. Reading from json is not supported. */
trait ActionDescriptorJsonProtocol extends DefaultJsonProtocol with IdJsonProtocol with SprayJsonSupport {

  class ActionDescriptorShortFormat extends RootJsonFormat[ActionDescriptor] {

    override def write(obj: ActionDescriptor): JsValue =
      JsObject("id" -> obj.id.toJson, "name" -> obj.name.toJson, "description" -> obj.description.toJson)

    override def read(json: JsValue): ActionDescriptor =
      throw new UnsupportedOperationException

  }

  /** Only id and name of operation. */
  object ActionDescriptorShortFormat extends ActionDescriptorShortFormat

  class ActionDescriptorBaseFormat extends ActionDescriptorShortFormat {

    override def write(obj: ActionDescriptor): JsValue = {
      JsObject(
        super.write(obj).asJsObject.fields ++ Map(
          "category"         -> obj.category.id.toJson,
          "description"      -> obj.description.toJson,
          "deterministic"    -> false.toJson, // TODO use real value as soon as it is supported
          "hasDocumentation" -> obj.hasDocumentation.toJson,
          "ports"            -> JsObject(
            "input"  -> portTypesToJson(obj.inPorts, addRequiredField = true, obj.inPortsLayout),
            "output" -> portTypesToJson(obj.outPorts, addRequiredField = false, obj.outPortsLayout)
          )
        )
      )
    }

    private def portTypesToJson(
        portTypes: Seq[Type],
        addRequiredField: Boolean,
        portsLayout: Vector[PortPosition]
    ): JsValue = {
      val required = if (addRequiredField) Some(true) else None
      // TODO use real value as soon as it is supported

      val fields =
        for ((portType, positioning, index) <- (portTypes, portsLayout, Stream.from(0)).zipped)
          yield portToJson(index, required, portType, positioning)

      fields.toList.toJson
    }

    implicit private val positionSerializer = EnumerationSerializer.jsonEnumFormat(PortPosition)

    private def portToJson(index: Int, required: Option[Boolean], portType: Type, position: PortPosition): JsValue = {
      val fields = Map(
        "portIndex"     -> index.toJson,
        "typeQualifier" -> TypeUtils.describeType(portType).toJson,
        "portPosition"  -> position.toJson
      )
      JsObject(required match {
        case Some(value) => fields.updated("required", value.toJson)
        case None        => fields
      })
    }

  }

  /** All operation's info except for parameters. */
  object ActionDescriptorBaseFormat extends ActionDescriptorBaseFormat

  /** Full operation's info. */
  object ActionDescriptorFullFormat extends ActionDescriptorBaseFormat {

    override def write(obj: ActionDescriptor): JsValue =
      JsObject(super.write(obj).asJsObject.fields.updated("parameters", obj.parametersJsonDescription))

  }

}

object ActionDescriptorJsonProtocol extends ActionDescriptorJsonProtocol
