package io.deepsense.deeplang.catalogs.doperations

import scala.reflect.runtime.universe.Type

import spray.json.JsValue

import io.deepsense.deeplang.{TypeUtils, DOperation}
import io.deepsense.deeplang.DPortPosition.DPortPosition

/**
 * Represents a registered DOperation and stores its name and i/o port types.
 */
case class DOperationDescriptor(
    id: DOperation.Id,
    name: String,
    description: String,
    category: DOperationCategory,
    hasDocumentation: Boolean,
    parametersJsonDescription: JsValue,
    inPorts: Seq[Type],
    inPortsLayout: Vector[DPortPosition],
    outPorts: Seq[Type],
    outPortsLayout: Vector[DPortPosition]) {

  override def toString: String = {
    def portsToString(ports: Seq[Type]): String = {
      ports.map(TypeUtils.typeToString).mkString(", ")
    }
    s"$name(${portsToString(inPorts)} => ${portsToString(outPorts)})"
  }
}
