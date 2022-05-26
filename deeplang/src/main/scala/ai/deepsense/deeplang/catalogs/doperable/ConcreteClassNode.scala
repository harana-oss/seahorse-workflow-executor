package ai.deepsense.deeplang.catalogs.doperable

import java.lang.reflect.Constructor

import ai.deepsense.deeplang.catalogs.doperable.exceptions.NoParameterlessConstructorInClassException
import ai.deepsense.deeplang.DOperable
import ai.deepsense.deeplang.TypeUtils

private[doperable] class ConcreteClassNode(javaType: Class[_]) extends ClassNode(javaType) {

  val constructor: Constructor[_] = TypeUtils.constructorForClass(javaType) match {
    case Some(parameterLessConstructor) => parameterLessConstructor
    case None                           => throw NoParameterlessConstructorInClassException(this.javaTypeName)
  }

  /** Creates instance of type represented by this. Invokes first constructor and assumes that it takes no parameters.
    */
  private[doperable] def createInstance[T <: DOperable]: T =
    TypeUtils.createInstance[T](constructor.asInstanceOf[Constructor[T]])

  override private[doperable] def subclassesInstances: Set[ConcreteClassNode] =
    super.subclassesInstances + this

}
