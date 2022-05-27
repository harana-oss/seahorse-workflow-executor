package ai.deepsense.deeplang.catalogs.actionobjects

import ai.deepsense.deeplang.utils.TypeUtils
import scala.reflect.runtime.{universe => ru}

/** Node that represents trait in hierarchy stored in ActionObjectCatalog. */
private[actionobjects] class TraitNode(override protected val javaType: Class[_]) extends TypeNode {

  override private[actionobjects] def getParentJavaType(upperBoundType: ru.Type): Option[Class[_]] = {
    val t             = TypeUtils.classToType(javaType)
    val baseTypes     = t.baseClasses.map(TypeUtils.symbolToType)
    val mirror        = TypeUtils.classMirror(javaType)
    val baseJavaTypes = baseTypes.filter(_ <:< upperBoundType).map(TypeUtils.typeToClass(_, mirror))
    baseJavaTypes.find(!_.isInterface)
  }

  override private[actionobjects] def descriptor: TypeDescriptor =
    TraitDescriptor(fullName, (supertraits.values ++ parent).map(_.fullName).toList)

}

private[actionobjects] object TraitNode {

  def apply(javaType: Class[_]): TraitNode = new TraitNode(javaType)

}
