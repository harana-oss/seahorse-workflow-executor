package io.deepsense.graph

import scala.reflect.runtime.{universe => ru}

import io.deepsense.deeplang.catalogs.doperable.DOperableCatalog
import io.deepsense.deeplang.DKnowledge
import io.deepsense.deeplang.DOperable
import io.deepsense.deeplang.DOperation

object DefaultKnowledgeService {

  /** @return Knowledge vector for output ports if no additional information is provided. */
  def defaultOutputKnowledge(catalog: DOperableCatalog, operation: DOperation): Vector[DKnowledge[DOperable]] =
    for (outPortType <- operation.outPortTypes) yield defaultKnowledge(catalog, outPortType)

  /** @return Knowledge vector for input ports if no additional information is provided. */
  def defaultInputKnowledge(catalog: DOperableCatalog, operation: DOperation): Vector[DKnowledge[DOperable]] =
    for (inPortType <- operation.inPortTypes) yield defaultKnowledge(catalog, inPortType)

  /** @return Knowledge for port if no additional information is provided. */
  def defaultKnowledge(catalog: DOperableCatalog, portType: ru.TypeTag[_]): DKnowledge[DOperable] = {
    val castedType = portType.asInstanceOf[ru.TypeTag[DOperable]]
    DKnowledge(catalog.concreteSubclassesInstances(castedType))
  }

}
