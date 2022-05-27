package ai.deepsense.deeplang.catalogs.actionobjects

import scala.reflect.runtime.{universe => ru}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import ai.deepsense.deeplang.ActionObject
import ai.deepsense.deeplang.catalogs.actionobjects.exceptions._
import ai.deepsense.deeplang.actionobjects.ActionObjectMock

object SampleInheritance {

  trait T1 extends ActionObject

  trait T2 extends T1

  trait T3 extends T1

  trait T extends ActionObject

  abstract class A extends T3

  case class B() extends A with T

  case class C() extends A with T2

}

object Parametrized {

  trait T[T] extends ActionObjectMock

  abstract class A[T] extends ActionObjectMock

  class B extends A[Int]

}

object Constructors {

  class NotParameterLess(val i: Int) extends ActionObjectMock

  class AuxiliaryParameterless(val i: Int) extends ActionObjectMock {

    def this() = this(1)

  }

  class WithDefault(val i: Int = 1) extends ActionObjectMock

}

object TraitInheritance {

  class C1 extends ActionObjectMock

  trait T1 extends C1

  trait T2 extends T1

  class C2 extends T2

  trait S1 extends ActionObject

  trait S2 extends ActionObject

  class A1 extends ActionObjectMock

  trait S3 extends A1 with S1 with S2

}

object MixinInheritance {

  trait P extends ActionObject

  trait TrA extends ActionObject

  trait TrB extends ActionObject

  class OpC extends ActionObject

  class OpA extends TrA with P

  class OpB extends TrB with P

}

class ActionObjectCatalogSuite extends AnyFunSuite with Matchers {

  def testGettingSubclasses[T <: ActionObject: ru.TypeTag](h: ActionObjectCatalog, expected: ActionObject*): Unit =
    h.concreteSubclassesInstances[T] should contain theSameElementsAs expected

  test("Getting concrete subclasses instances") {
    import SampleInheritance._

    val h = new ActionObjectCatalog
    h.registerActionObject[B]()
    h.registerActionObject[C]()

    val b = new B
    val c = new C

    def check[T <: ActionObject: ru.TypeTag](expected: ActionObject*): Unit =
      testGettingSubclasses[T](h, expected: _*)

    check[T with T1](b)
    check[T2 with T3](c)
    check[B](b)
    check[C](c)
    check[A with T2](c)
    check[A with T1](b, c)
    check[A](b, c)
    check[T3](b, c)
    check[T with T2]()
  }

  test("Getting concrete subclasses instances using ru.TypeTag") {
    import SampleInheritance._
    val h = new ActionObjectCatalog
    h.registerActionObject[B]()
    val t = ru.typeTag[T]
    h.concreteSubclassesInstances(t) should contain theSameElementsAs List(new B)
  }

  test("Listing DTraits and DClasses") {
    import SampleInheritance._
    val h = new ActionObjectCatalog
    h.registerActionObject[B]()
    h.registerActionObject[C]()

    def name[T: ru.TypeTag]: String = ru.typeOf[T].typeSymbol.fullName

    val traits = (TraitDescriptor(name[ActionObject], Nil) ::
      TraitDescriptor(name[T2], List(name[T1])) ::
      TraitDescriptor(name[T], List(name[ActionObject])) ::
      TraitDescriptor(name[T1], List(name[ActionObject])) ::
      TraitDescriptor(name[T3], List(name[T1])) ::
      Nil).map(t => t.name -> t).toMap

    val classes = (ClassDescriptor(name[A], None, List(name[T3])) ::
      ClassDescriptor(name[B], Some(name[A]), List(name[T])) ::
      ClassDescriptor(name[C], Some(name[A]), List(name[T2])) ::
      Nil).map(c => c.name -> c).toMap

    val descriptor = h.descriptor
    descriptor.traits should contain theSameElementsAs traits
    descriptor.classes should contain theSameElementsAs classes
  }

  test("Registering class extending parametrized class") {
    import ai.deepsense.deeplang.catalogs.actionobjects.Parametrized._
    val p = new ActionObjectCatalog
    p.registerActionObject[B]()
  }

  test("Registering parametrized class") {
    import ai.deepsense.deeplang.catalogs.actionobjects.Parametrized._
    val p = new ActionObjectCatalog
    p.registerActionObject[A[Int]]()
  }

  test("Registering parametrized trait") {
    import ai.deepsense.deeplang.catalogs.actionobjects.Parametrized._
    val p = new ActionObjectCatalog
    p.registerActionObject[T[Int]]()
  }

  test("Registering concrete class with no parameter-less constructor should produce exception") {
    intercept[NoParameterlessConstructorInClassException] {
      import ai.deepsense.deeplang.catalogs.actionobjects.Constructors._
      val h = new ActionObjectCatalog
      h.registerActionObject[NotParameterLess]()
    }
  }

  test("Registering class with constructor with default parameters should produce exception") {
    intercept[NoParameterlessConstructorInClassException] {
      import ai.deepsense.deeplang.catalogs.actionobjects.Constructors._
      val h = new ActionObjectCatalog
      h.registerActionObject[WithDefault]()
    }
  }

  test("Registering class with auxiliary parameterless constructor should succeed") {
    import ai.deepsense.deeplang.catalogs.actionobjects.Constructors._
    val h = new ActionObjectCatalog
    h.registerActionObject[AuxiliaryParameterless]()
  }

  test("It is possible to register a trait that extends a class") {
    import ai.deepsense.deeplang.catalogs.actionobjects.MixinInheritance._
    val h          = new ActionObjectCatalog
    h.registerActionObject[OpB]()
    h.registerActionObject[OpA]()
    val subclasses = h.concreteSubclassesInstances[P]
    assert(subclasses.size == 2)
    assert(subclasses.exists(x => x.isInstanceOf[OpA]))
    assert(subclasses.exists(x => x.isInstanceOf[OpB]))
  }

}
