package ai.deepsense.deeplang.catalogs.actionobjects

import ai.deepsense.deeplang.utils.TypeUtils

import scala.reflect.runtime.{universe => ru}

/** Node that represents class in hierarchy stored in ActionObjectCatalog. */
private[actionobjects] class ClassNode(override protected val javaType: Class[_]) extends TypeNode {

  def javaTypeName: String = javaType.getCanonicalName

  override private[actionobjects] def getParentJavaType(upperBoundType: ru.Type): Option[Class[_]] = {
    val parentJavaType = javaType.getSuperclass
    val parentType     = TypeUtils.classToType(parentJavaType)
    if (parentType <:< upperBoundType) Some(parentJavaType) else None
  }

  override private[actionobjects] def descriptor: TypeDescriptor = {
    val parentName = if (parent.isDefined) Some(parent.get.fullName) else None
    ClassDescriptor(fullName, parentName, supertraits.values.map(_.fullName).toList)
  }

}

private[actionobjects] object ClassNode {

  def apply(javaType: Class[_]): ClassNode =
    if (TypeUtils.isAbstract(javaType))
      new ClassNode(javaType)
    else
      new ConcreteClassNode(javaType)

}
