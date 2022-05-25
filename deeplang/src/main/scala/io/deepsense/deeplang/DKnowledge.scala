package io.deepsense.deeplang

import scala.reflect.runtime.{universe => ru}

/**
 * Represents knowledge about the set of possible types.
 * @param types The sequence of types.
 *              It is the caller responsibility to make sure they are distinct.
 * @tparam T The lowest common ancestor of input types
 */
class DKnowledge[+T <: DOperable] private[DKnowledge] (val types: Seq[T]) {

  /**
   * Returns a DKnowledge with types that are subtypes of given Type.
   */
  def filterTypes(t: ru.Type): DKnowledge[T] = {
    DKnowledge(types.filter(x => TypeUtils.classToType(x.getClass) <:< t): _*)
  }

  def ++[U >: T <: DOperable](other: DKnowledge[U]): DKnowledge[U] = {
    DKnowledge[U](types ++ other.types: _*)
  }

  override def equals(other: Any): Boolean = {
    other match {
      case that: DKnowledge[_] => types.toSet == that.types.toSet
      case _ => false
    }
  }

  /**
   * Returns first type from DKnowledge.
   * Throws exception when there are None.
   */
  def single: T = {
    require(types.nonEmpty, "Expected at least one inferred type, but got 0")
    types.head
  }

  def size: Int = types.size

  override def hashCode(): Int = types.hashCode()

  override def toString: String = s"DKnowledge($types)"
}

object DKnowledge {
  def apply[T <: DOperable](args: T*)(implicit s: DummyImplicit): DKnowledge[T] =
    new DKnowledge[T](args.distinct)

  def apply[T <: DOperable](types: Seq[T]): DKnowledge[T] = new DKnowledge[T](types.distinct)

  def apply[T <: DOperable](types: Set[T]): DKnowledge[T] = new DKnowledge[T](types.toSeq)

  def apply[T <: DOperable](dKnowledges: Traversable[DKnowledge[T]]): DKnowledge[T] =
    dKnowledges.foldLeft(new DKnowledge[T](Seq.empty))(_ ++ _)
}
