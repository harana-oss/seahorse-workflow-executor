package ai.deepsense.deeplang.catalogs.actions

import ai.deepsense.deeplang.ActionObject
import ai.deepsense.deeplang.Action
import ai.deepsense.deeplang.ActionCategories
import ai.deepsense.deeplang.catalogs.actions.exceptions._
import ai.deepsense.deeplang.actions.UnknownOperation
import scala.collection.mutable

import ai.deepsense.deeplang.catalogs.SortPriority
import ai.deepsense.deeplang.catalogs.spi.CatalogRegistrar.OperationFactory
import javassist.bytecode.stackmap.TypeTag

/** Catalog of Actions. Allows to register Actions under specified categories, to browse categories structure
  * and to create operations instances.
  */
abstract class ActionCatalog {

  /** Tree describing categories structure. */
  def categoryTree: DOperationCategoryNode

  /** Map of all registered operation descriptors, where their ids are keys. */
  def operations: Map[Action.Id, ActionDescriptor]

  /** Creates instance of requested DOperation class.
    * @param id
    *   id that identifies desired DOperation
    */
  def createDOperation(id: Action.Id): Action

  /** Registers DOperation, which can be later viewed and created.
    * @param category
    *   category to which this operation directly belongs
    * @param visible
    *   a flag determining if the operation should be visible in the category tree
    */
  def registerDOperation(
                          category: ActionCategory,
                          factory: () => Action,
                          priority: SortPriority,
                          visible: Boolean = true
  ): Unit

  /** Fetch the categories used by the registered operations in this catalog. */
  def categories: Seq[ActionCategory] = categoryTree.getCategories

  def registeredOperations: Seq[(ActionCategory, OperationFactory, SortPriority)]

}

object ActionCatalog {

  def apply(): ActionCatalog = new ActionCatalogImpl

  private class ActionCatalogImpl() extends ActionCatalog {

    var categoryTree = DOperationCategoryNode()

    var operations = Map.empty[Action.Id, ActionDescriptor]

    private val operationFactoryByOperationId = mutable.Map.empty[Action.Id, () => Action]

    def registeredOperations: Seq[(ActionCategory, OperationFactory, SortPriority)] =
      categoryTree.getOperations.map { case (category: ActionCategory, id: Action.Id, priority: SortPriority) =>
        (category, operationFactoryByOperationId(id), priority)
      }

    def registerDOperation(
                            category: ActionCategory,
                            factory: () => Action,
                            priority: SortPriority,
                            visible: Boolean = true
    ): Unit = {
      val operationInstance    = factory()
      operationInstance.validate()
      val id                   = operationInstance.id
      val name                 = operationInstance.name
      val description          = operationInstance.description
      val inPortTypes          = operationInstance.inPortTypes.map(_.tpe)
      val outPortTypes         = operationInstance.outPortTypes.map(_.tpe)
      val parameterDescription = operationInstance.paramsToJson
      val operationDescriptor  = ActionDescriptor(
        id,
        name,
        description,
        category,
        priority,
        operationInstance.hasDocumentation,
        parameterDescription,
        inPortTypes,
        operationInstance.inPortsLayout,
        outPortTypes,
        operationInstance.outPortsLayout
      )

      if (operations.contains(id)) {
        val alreadyRegisteredOperation = operations(id)
        throw new RuntimeException(
          s"Trying to register operation '$name' with UUID $id, " +
            s"but there is already operation '${alreadyRegisteredOperation.name}' with the same UUID value. " +
            s"Please change UUID of one of them."
        )
      }
      operations += id -> operationDescriptor
      if (visible)
        categoryTree = categoryTree.addOperation(operationDescriptor, category)
      operationFactoryByOperationId(id) = factory
    }

    def createDOperation(id: Action.Id): Action = operationFactoryByOperationId.get(id) match {
      case Some(factory) => factory()
      case None          => throw DOperationNotFoundException(id)
    }

    // This is a special case operation that serves as a fallback when an unrecognized
    // UUID is encountered in a workflow. It is registered here so that the visibility flag
    // can be set to false.
    registerDOperation(
      ActionCategories.Other,
      () => new UnknownOperation,
      SortPriority.coreDefault,
      visible = false
    )

  }

}
