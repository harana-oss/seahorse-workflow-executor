package ai.deepsense.deeplang.catalogs.actions

import scala.collection.immutable.SortedMap

import ai.deepsense.deeplang.Action
import ai.deepsense.deeplang.catalogs.SortPriority
import ai.deepsense.deeplang.catalogs.actions.ActionCategoryNode.InnerNodes

/** Node in ActionCategoryTree. Represents certain category, holds its subcategories and assigned operations.
  * Objects of this class are immutable.
  * @param category
  *   category represented by this node or None if it is root
  * @param successors
  *   map from all direct child-categories to nodes representing them
  * @param operations
  *   operations directly in category represented by this node
  */
case class ActionCategoryNode(
                                   category: Option[ActionCategory] = None,
                                   successors: InnerNodes = ActionCategoryNode.emptyInnerNodes,
                                   operations: List[ActionDescriptor] = List.empty
) {

  /** Adds operation to node under given path of categories.
    * @param operation
    *   descriptor of operation to be added
    * @param path
    *   requested path of categories from this node to added operation
    * @return
    *   node identical to this but with operation added
    */
  private def addOperationAtPath(
                                  operation: ActionDescriptor,
                                  path: List[ActionCategory]
  ): ActionCategoryNode = {
    path match {
      case Nil              => copy(operations = (operations :+ operation).sortWith(_.priority < _.priority))
      case category :: tail =>
        val successor        = successors.getOrElse(category, ActionCategoryNode(Some(category)))
        val updatedSuccessor = successor.addOperationAtPath(operation, tail)
        copy(successors = successors + (category -> updatedSuccessor))
    }
  }

  /** Adds a new Action to the tree represented by this node under a specified category.
    * @param operation
    *   operation descriptor to be added
    * @param category
    *   category under which operation should directly be
    * @return
    *   category tree identical to this but with operation added
    */
  def addOperation(operation: ActionDescriptor, category: ActionCategory): ActionCategoryNode =
    addOperationAtPath(operation, category.pathFromRoot)

  /** Gets all operations inside a tree
    * @return
    *   Sequence of tuples containing operation with its priority and category
    */
  def getOperations: Seq[(ActionCategory, Action.Id, SortPriority)] = {
    val thisOperations      = operations.map(operation => (operation.category, operation.id, operation.priority))
    val successorOperations = successors.collect { case successor => successor._2.getOperations }.flatten
    thisOperations ++ successorOperations
  }

  /** Gets all categories inside a tree
    * @return
    *   Sequence of registrated categories
    */
  def getCategories: Seq[ActionCategory] = {
    val successorOperations = successors.collect { case successor => successor._2.getCategories }.flatten
    successorOperations.toList ++ category
  }

}

object ActionCategoryNode {

  type InnerNodes = SortedMap[ActionCategory, ActionCategoryNode]

  def emptyInnerNodes = SortedMap[ActionCategory, ActionCategoryNode]()

}
