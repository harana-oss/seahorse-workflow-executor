package ai.deepsense.deeplang.catalogs.actions

import scala.reflect.runtime.universe.Type

import spray.json.JsValue
import ai.deepsense.deeplang.Action
import ai.deepsense.deeplang.PortPosition.PortPosition
import ai.deepsense.deeplang.catalogs.SortPriority
import ai.deepsense.deeplang.utils.TypeUtils

/** Represents a registered DOperation and stores its name and i/o port types. */
case class ActionDescriptor(
                             id: Action.Id,
                             name: String,
                             description: String,
                             category: ActionCategory,
                             priority: SortPriority,
                             hasDocumentation: Boolean,
                             parametersJsonDescription: JsValue,
                             inPorts: Seq[Type],
                             inPortsLayout: Vector[PortPosition],
                             outPorts: Seq[Type],
                             outPortsLayout: Vector[PortPosition]
) {

  override def toString: String = {
    def portsToString(ports: Seq[Type]): String =
      ports.map(TypeUtils.typeToString).mkString(", ")
    s"$name(${portsToString(inPorts)} => ${portsToString(outPorts)})"
  }

}
