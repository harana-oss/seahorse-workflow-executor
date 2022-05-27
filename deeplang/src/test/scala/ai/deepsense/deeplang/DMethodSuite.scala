package ai.deepsense.deeplang

import org.scalatest.funsuite.AnyFunSuite

import ai.deepsense.deeplang.catalogs.actionobjects.ActionObjectCatalog
import ai.deepsense.deeplang.actionobjects.ActionObjectMock
import ai.deepsense.deeplang.inference.InferContext
import ai.deepsense.deeplang.inference.InferenceWarnings

object DClassesForDMethods {

  class S extends ActionObjectMock

  case class A(i: Int) extends S { def this() = this(0) }

  case class B(i: Int) extends S { def this() = this(0) }

}

class DMethodSuite extends AnyFunSuite with DeeplangTestSupport {

  test("It is possible to implement class having DMethod") {
    import DClassesForDMethods._

    class C extends ActionObjectMock {
      val f: DMethod1To1[Int, A, B] = new DMethod1To1[Int, A, B] {
        override def apply(context: ExecutionContext)(parameters: Int)(t0: A): B =
          B(t0.i + parameters)
      }
    }

    val c = new C
    assert(c.f(mock[ExecutionContext])(2)(A(3)) == B(5))

    val h = new ActionObjectCatalog
    h.registerActionObject[A]()
    h.registerActionObject[B]()

    val context            = createInferContext(h)
    val (result, warnings) = c.f.infer(context)(2)(Knowledge(new A()))
    assert(result == Knowledge(new B()))
    assert(warnings == InferenceWarnings.empty)
  }

  test("It is possible to override inferring in DMethod") {
    import DClassesForDMethods._

    val mockedWarnings = mock[InferenceWarnings]

    class C extends ActionObjectMock {
      val f: DMethod0To1[Int, S] = new DMethod0To1[Int, S] {
        override def apply(context: ExecutionContext)(parameters: Int)(): S                              = A(parameters)
        override def infer(context: InferContext)(parameters: Int)(): (Knowledge[S], InferenceWarnings) =
          (Knowledge(new A), mockedWarnings)
      }
    }

    val c = new C

    val h = new ActionObjectCatalog
    h.registerActionObject[A]()
    h.registerActionObject[B]()

    val context            = createInferContext(h)
    val (result, warnings) = c.f.infer(context)(2)()
    assert(result == Knowledge(new A()))
    assert(warnings == mockedWarnings)
  }

}
