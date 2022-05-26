package ai.deepsense.deeplang

import scala.reflect.runtime.{universe => ru}

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import ai.deepsense.deeplang.doperables.DOperableMock

object ClassesForDKnowledge {

  trait A extends DOperableMock

  trait B extends DOperableMock

  case class A1(i: Int) extends A

  case class A2(i: Int) extends A

  case class B1(i: Int) extends B

  case class B2(i: Int) extends B

}

class DKnowledgeSuite extends AnyFunSuite with Matchers {

  test("DKnowledge[DOperable] with same content are equal") {
    case class A(i: Int) extends DOperableMock
    case class B(i: Int) extends DOperableMock

    val knowledge1 = DKnowledge(A(1), B(2), A(3))
    val knowledge2 = DKnowledge(A(1), A(3), B(2), A(1))
    knowledge1 shouldBe knowledge2
  }

  test("DKnowledge[_] objects with same content are equal") {

    def isAOrB(any: Any): Boolean = any.isInstanceOf[A] || any.isInstanceOf[B]

    class A extends DOperableMock {
      override def equals(any: Any): Boolean = isAOrB(any)
      override def hashCode: Int             = 1234567
    }
    class B extends DOperableMock {
      override def equals(any: Any): Boolean = isAOrB(any)
      override def hashCode: Int             = 1234567
    }

    val knowledge1: DKnowledge[A] = DKnowledge(new A)
    val knowledge2: DKnowledge[B] = DKnowledge(new B)

    knowledge1 shouldBe knowledge2
  }

  test("DKnowledge with different content are not equal") {
    case class A(i: Int) extends DOperableMock

    val knowledge1 = DKnowledge(A(1))
    val knowledge2 = DKnowledge(A(2))
    knowledge1 shouldNot be(knowledge2)
  }

  test("DKnowledge can intersect internal knowledge with external types") {
    import ClassesForDKnowledge._
    val knowledge = DKnowledge(A1(1), A2(2), B1(1), B2(2))
    knowledge.filterTypes(ru.typeOf[A]) shouldBe DKnowledge(A1(1), A2(2))
  }

  test("Sum of two DKnowledges is sum of their types") {
    import ClassesForDKnowledge._
    val knowledge1           = DKnowledge[A](A1(1), A2(2))
    val knowledge2           = DKnowledge[B](B1(1), B2(2))
    val expectedKnowledgeSum = DKnowledge[DOperable](A1(1), A2(2), B1(1), B2(2))
    val actualKnowledgeSum   = knowledge1 ++ knowledge2

    actualKnowledgeSum shouldBe expectedKnowledgeSum
  }

  test("DKnowledge can be constructed of traversable of DKnowledges") {
    import ClassesForDKnowledge._
    val knowledge1   = DKnowledge[A](A1(1))
    val knowledge2   = DKnowledge[A](A2(2))
    val knowledgeSum = DKnowledge(A1(1), A2(2))
    DKnowledge(Traversable(knowledge1, knowledge2)) shouldBe knowledgeSum
  }

}
