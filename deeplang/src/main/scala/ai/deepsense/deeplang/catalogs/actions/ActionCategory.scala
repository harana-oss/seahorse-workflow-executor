package ai.deepsense.deeplang.catalogs.actions

import ai.deepsense.commons.models
import ai.deepsense.deeplang.catalogs.SortPriority

/** Category of Actions. It can be subcategory of different DOperationCategory.
  * @param id
  *   id of this category
  * @param name
  *   display name of this category
  * @param parent
  *   super-category of this one, if None this is top-level category
  *
  * TODO use global id class when available
  */
abstract class ActionCategory(
                               val id: ActionCategory.Id,
                               val name: String,
                               val priority: SortPriority,
                               val parent: Option[ActionCategory] = None
) extends Ordered[ActionCategory] {

  def this(id: ActionCategory.Id, name: String, priority: SortPriority, parent: ActionCategory) =
    this(id, name, priority, Some(parent))

  /** List of categories on path from this category to some top-level category. */
  private[actions] def pathToRoot: List[ActionCategory] = parent match {
    case Some(category) => this :: category.pathToRoot
    case None           => List(this)
  }

  /** List of categories on path from some top-level category to this category. */
  private[actions] def pathFromRoot: List[ActionCategory] = pathToRoot.reverse

  override def compare(o: ActionCategory) = {
    priority.compare(o.priority) match {
      case 0          => if (this.equals(o)) 0 else 1
      case other: Int => other
    }
  }

}

object ActionCategory {

  type Id = models.Id

  val Id = models.Id

}
