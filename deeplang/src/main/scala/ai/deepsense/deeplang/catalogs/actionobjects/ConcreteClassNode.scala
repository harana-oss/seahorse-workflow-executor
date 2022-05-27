package ai.deepsense.deeplang.catalogs.actionobjects

import java.lang.reflect.Constructor

import ai.deepsense.deeplang.catalogs.actionobjects.exceptions.NoParameterlessConstructorInClassException
import ai.deepsense.deeplang.ActionObject
import ai.deepsense.deeplang.utils.TypeUtils

private[actionobjects] class ConcreteClassNode(javaType: Class[_]) extends ClassNode(javaType) {

  val constructor: Constructor[_] = TypeUtils.constructorForClass(javaType) match {
    case Some(parameterLessConstructor) => parameterLessConstructor
    case None                           => throw NoParameterlessConstructorInClassException(this.javaTypeName)
  }

  /** Creates instance of type represented by this. Invokes first constructor and assumes that it takes no parameters.
    */
  private[actionobjects] def createInstance[T <: ActionObject]: T =
    TypeUtils.createInstance[T](constructor.asInstanceOf[Constructor[T]])

  override private[actionobjects] def subclassesInstances: Set[ConcreteClassNode] =
    super.subclassesInstances + this

}
