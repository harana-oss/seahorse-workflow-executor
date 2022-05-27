package ai.deepsense.deeplang

import scala.reflect.runtime.{universe => ru}

import org.scalatest.funsuite.AnyFunSuite

import ai.deepsense.commons.utils.Version
import ai.deepsense.deeplang.catalogs.actionobjects.ActionObjectCatalog
import ai.deepsense.deeplang.actionobjects.ActionObjectMock
import ai.deepsense.deeplang.inference.InferContext
import ai.deepsense.deeplang.inference.InferenceWarnings
import ai.deepsense.deeplang.parameters.NumericParameter
import ai.deepsense.deeplang.parameters.validators.RangeValidator

object DClassesForActions {

  trait A extends ActionObjectMock

  case class A1() extends A

  case class A2() extends A

}

object ActionForPortTypes {

  import DClassesForActions._

  class SimpleOperation extends Action1To1[A1, A2] {

    override protected def execute(t0: A1)(context: ExecutionContext): A2 = ???

    override val id: Action.Id = Action.Id.randomId

    override val name: String = ""

    override val description: String = ""

    override val specificParams: Array[ai.deepsense.deeplang.parameters.Parameter[_]] = Array()

    override lazy val tTagTI_0: ru.TypeTag[A1] = ru.typeTag[A1]

    override lazy val tTagTO_0: ru.TypeTag[A2] = ru.typeTag[A2]

  }

}

class ActionSuite extends AnyFunSuite with DeeplangTestSupport {

  test("It is possible to implement simple operations") {
    import DClassesForActions._

    class PickOne extends Action2To1[A1, A2, A] {
      override val id: Action.Id = Action.Id.randomId

      val param                         = NumericParameter("param", None, RangeValidator.all)
      def setParam(int: Int): this.type = set(param -> int)

      val specificParams: Array[ai.deepsense.deeplang.parameters.Parameter[_]] = Array(param)

      override protected def execute(t1: A1, t2: A2)(context: ExecutionContext): A =
        if ($(param) % 2 == 1) t1 else t2
      override val name: String                                                    = "Some name"
      override val description: String                                             = "Some description"

      override lazy val tTagTI_0: ru.TypeTag[A1] = ru.typeTag[A1]
      override lazy val tTagTO_0: ru.TypeTag[A]  = ru.typeTag[A]
      override lazy val tTagTI_1: ru.TypeTag[A2] = ru.typeTag[A2]
    }

    val firstPicker  = new PickOne
    firstPicker.setParam(1)
    val secondPicker = new PickOne
    secondPicker.setParam(2)

    val input = Vector(A1(), A2())
    assert(firstPicker.executeUntyped(input)(mock[ExecutionContext]) == Vector(A1()))
    assert(secondPicker.executeUntyped(input)(mock[ExecutionContext]) == Vector(A2()))

    val h       = new ActionObjectCatalog
    h.registerActionObject[A1]()
    h.registerActionObject[A2]()
    val context = createInferContext(h)

    val knowledge          = Vector[Knowledge[ActionObject]](Knowledge(A1()), Knowledge(A2()))
    val (result, warnings) = firstPicker.inferKnowledgeUntyped(knowledge)(context)
    assert(result == Vector(Knowledge(A1(), A2())))
    assert(warnings == InferenceWarnings.empty)
  }

  test("It is possible to override knowledge inferring in Action") {
    import DClassesForActions._

    val mockedWarnings = mock[InferenceWarnings]

    class GeneratorOfA extends Action0To1[A] {
      override val id: Action.Id = Action.Id.randomId

      override protected def execute()(context: ExecutionContext): A                                     = ???
      override protected def inferKnowledge()(context: InferContext): (Knowledge[A], InferenceWarnings) =
        (Knowledge(A1(), A2()), mockedWarnings)

      override val name: String        = ""
      override val description: String = ""

      val specificParams: Array[ai.deepsense.deeplang.parameters.Parameter[_]] = Array()

      override lazy val tTagTO_0: ru.TypeTag[A] = ru.typeTag[A]
    }

    val generator: Action = new GeneratorOfA

    val h       = new ActionObjectCatalog
    h.registerActionObject[A1]()
    h.registerActionObject[A2]()
    val context = createInferContext(h)

    val (results, warnings) = generator.inferKnowledgeUntyped(Vector())(context)
    assert(results == Vector(Knowledge(A1(), A2())))
    assert(warnings == mockedWarnings)
  }

  test("Getting types required in input port") {
    import ActionForPortTypes._
    val op = new SimpleOperation
    assert(op.inPortTypes == Vector(ru.typeTag[DClassesForActions.A1]))
  }

  test("Getting types required in output port") {
    import ActionForPortTypes._
    val op = new SimpleOperation
    assert(op.outPortTypes == Vector(ru.typeTag[DClassesForActions.A2]))
  }

}
