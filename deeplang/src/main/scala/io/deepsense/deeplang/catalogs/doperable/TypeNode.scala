package io.deepsense.deeplang.catalogs.doperable

import scala.collection.mutable
import scala.reflect.runtime.{universe => ru}

import io.deepsense.deeplang.TypeUtils

/** Node that represents type in hierarchy stored in DOperableCatalog. */
abstract private[doperable] class TypeNode {

  /** Points to type corresponding to this node. */
  protected val javaType: Class[_]

  /** Informs if this type is a trait. */
  private[doperable] val isTrait: Boolean = javaType.isInterface

  /** Direct superclass of this type, if any. */
  protected var parent: Option[TypeNode] = None

  /** All direct supertraits of this type. Keys are type nodes fullNames. */
  protected val supertraits: mutable.Map[String, TypeNode] = mutable.Map()

  /** All direct superclasses of this type. Keys are type nodes fullNames. */
  protected val subclasses: mutable.Map[String, TypeNode] = mutable.Map()

  /** All direct subtraits of this type. Keys are type nodes fullNames. */
  protected val subtraits: mutable.Map[String, TypeNode] = mutable.Map()

  /** Name that unambiguously defines underlying type. */
  private[doperable] val fullName: String = javaType.getName.replaceAllLiterally("$", ".")

  private[doperable] def setParent(node: TypeNode): Unit = parent = Some(node)

  private[doperable] def addSupertrait(node: TypeNode): Unit = supertraits(node.fullName) = node

  /** Adds type as direct subtype (subtrait or subclass) of this type. */
  private[doperable] def addSuccessor(node: TypeNode): Unit =
    if (node.isTrait) addSubtrait(node) else addSubclass(node)

  private def addSubclass(node: TypeNode): Unit = subclasses(node.fullName) = node

  private def addSubtrait(node: TypeNode): Unit = subtraits(node.fullName) = node

  /** Returns java type of parent class of node if such parent exists and is subtype of provided 'upperBoundType'. */
  private[doperable] def getParentJavaType(upperBoundType: ru.Type): Option[Class[_]]

  private[doperable] def descriptor: TypeDescriptor

  /** Returns set of all concrete nodes that are descendants of this. */
  private[doperable] def subclassesInstances: Set[ConcreteClassNode] = {
    val descendants = subclasses.values.map(_.subclassesInstances) ++
      subtraits.values.map(_.subclassesInstances)
    TypeNode.sumSets[ConcreteClassNode](descendants)
  }

}

private[doperable] object TypeNode {

  def apply(javaType: Class[_]): TypeNode =
    if (javaType.isInterface) TraitNode(javaType) else ClassNode(javaType)

  /** Returns sum of sequence of sets. */
  private[TypeNode] def sumSets[T](sets: Iterable[Set[T]]): Set[T] =
    sets.foldLeft(Set[T]())((x, y) => x ++ y)

}
