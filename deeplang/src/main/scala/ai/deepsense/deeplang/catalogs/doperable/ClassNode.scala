package ai.deepsense.deeplang.catalogs.doperable

import scala.reflect.runtime.{universe => ru}

import ai.deepsense.deeplang.TypeUtils

/** Node that represents class in hierarchy stored in DOperableCatalog. */
private[doperable] class ClassNode(override protected val javaType: Class[_]) extends TypeNode {

  def javaTypeName: String = javaType.getCanonicalName

  override private[doperable] def getParentJavaType(upperBoundType: ru.Type): Option[Class[_]] = {
    val parentJavaType = javaType.getSuperclass
    val parentType     = TypeUtils.classToType(parentJavaType)
    if (parentType <:< upperBoundType) Some(parentJavaType) else None
  }

  override private[doperable] def descriptor: TypeDescriptor = {
    val parentName = if (parent.isDefined) Some(parent.get.fullName) else None
    ClassDescriptor(fullName, parentName, supertraits.values.map(_.fullName).toList)
  }

}

private[doperable] object ClassNode {

  def apply(javaType: Class[_]): ClassNode =
    if (TypeUtils.isAbstract(javaType))
      new ClassNode(javaType)
    else
      new ConcreteClassNode(javaType)

}
