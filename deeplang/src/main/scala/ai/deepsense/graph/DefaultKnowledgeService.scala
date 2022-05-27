package ai.deepsense.graph

import scala.reflect.runtime.{universe => ru}

import ai.deepsense.deeplang.catalogs.actionobjects.ActionObjectCatalog
import ai.deepsense.deeplang.Knowledge
import ai.deepsense.deeplang.ActionObject
import ai.deepsense.deeplang.Action

object DefaultKnowledgeService {

  /** @return Knowledge vector for output ports if no additional information is provided. */
  def defaultOutputKnowledge(catalog: ActionObjectCatalog, operation: Action): Vector[Knowledge[ActionObject]] =
    for (outPortType <- operation.outPortTypes) yield defaultKnowledge(catalog, outPortType)

  /** @return Knowledge vector for input ports if no additional information is provided. */
  def defaultInputKnowledge(catalog: ActionObjectCatalog, operation: Action): Vector[Knowledge[ActionObject]] =
    for (inPortType <- operation.inPortTypes) yield defaultKnowledge(catalog, inPortType)

  /** @return Knowledge for port if no additional information is provided. */
  def defaultKnowledge(catalog: ActionObjectCatalog, portType: ru.TypeTag[_]): Knowledge[ActionObject] = {
    val castedType = portType.asInstanceOf[ru.TypeTag[ActionObject]]
    Knowledge(catalog.concreteSubclassesInstances(castedType))
  }

}
