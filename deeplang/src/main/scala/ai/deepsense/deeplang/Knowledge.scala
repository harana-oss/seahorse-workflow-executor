package ai.deepsense.deeplang

import ai.deepsense.deeplang.utils.TypeUtils

import scala.reflect.runtime.{universe => ru}

/** Represents knowledge about the set of possible types.
  * @param types
  *   The sequence of types. It is the caller responsibility to make sure they are distinct.
  * @tparam T
  *   The lowest common ancestor of input types
  */
class Knowledge[+T <: ActionObject] private[Knowledge](val types: Seq[T]) {

  /** Returns a DKnowledge with types that are subtypes of given Type. */
  def filterTypes(t: ru.Type): Knowledge[T] =
    Knowledge(types.filter(x => TypeUtils.classToType(x.getClass) <:< t): _*)

  def ++[U >: T <: ActionObject](other: Knowledge[U]): Knowledge[U] =
    Knowledge[U](types ++ other.types: _*)

  override def equals(other: Any): Boolean = {
    other match {
      case that: Knowledge[_] => types.toSet == that.types.toSet
      case _                   => false
    }
  }

  /** Returns first type from DKnowledge. Throws exception when there are None. */
  def single: T = {
    require(types.nonEmpty, "Expected at least one inferred type, but got 0")
    types.head
  }

  def size: Int = types.size

  override def hashCode(): Int = types.hashCode()

  override def toString: String = s"DKnowledge($types)"

}

object Knowledge {

  def apply[T <: ActionObject](args: T*)(implicit s: DummyImplicit): Knowledge[T] =
    new Knowledge[T](args.distinct)

  def apply[T <: ActionObject](types: Seq[T]): Knowledge[T] = new Knowledge[T](types.distinct)

  def apply[T <: ActionObject](types: Set[T]): Knowledge[T] = new Knowledge[T](types.toSeq)

  def apply[T <: ActionObject](dKnowledges: Traversable[Knowledge[T]]): Knowledge[T] =
    dKnowledges.foldLeft(new Knowledge[T](Seq.empty))(_ ++ _)

}
