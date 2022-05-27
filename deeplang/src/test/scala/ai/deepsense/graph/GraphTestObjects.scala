package ai.deepsense.graph

import scala.reflect.runtime.{universe => ru}

import org.scalatestplus.mockito.MockitoSugar

import ai.deepsense.commons.utils.Version
import ai.deepsense.deeplang._
import ai.deepsense.deeplang.actionobjects.ActionObjectMock
import ai.deepsense.graph.FlowGraph.FlowNode

object RandomNodeFactory {

  def randomNode(operation: Action): FlowNode = Node(Node.Id.randomId, operation)

}

object DClassesForActions extends MockitoSugar {

  trait A extends ActionObjectMock

  case class A1() extends A

  case class A2() extends A

}

object ActionTestClasses {

  import ai.deepsense.graph.DClassesForActions._

  trait ActionBaseFields extends Action {

    // NOTE: id will be different for each instance
    override val id: Action.Id = Action.Id.randomId

    override val name: String = ""

    override val description: String = ""

    val specificParams: Array[ai.deepsense.deeplang.parameters.Parameter[_]] = Array()

  }

  case class ActionCreateA1() extends Action0To1[A1] with ActionBaseFields {

    override protected def execute()(context: ExecutionContext): A1 = ???

    @transient
    override lazy val tTagTO_0: ru.TypeTag[A1] = ru.typeTag[A1]

  }

  case class ActionReceiveA1() extends Action1To0[A1] with ActionBaseFields {

    override protected def execute(t0: A1)(context: ExecutionContext): Unit = ???

    @transient
    override lazy val tTagTI_0: ru.TypeTag[A1] = ru.typeTag[A1]

  }

  case class ActionA1ToA() extends Action1To1[A1, A] with ActionBaseFields {

    override protected def execute(t1: A1)(context: ExecutionContext): A = ???

    @transient
    override lazy val tTagTI_0: ru.TypeTag[A1] = ru.typeTag[A1]

    @transient
    override lazy val tTagTO_0: ru.TypeTag[A] = ru.typeTag[A]

  }

  case class ActionAToA1A2() extends Action1To2[A, A1, A2] with ActionBaseFields {

    override protected def execute(in: A)(context: ExecutionContext): (A1, A2) = ???

    @transient
    override lazy val tTagTI_0: ru.TypeTag[A] = ru.typeTag[A]

    @transient
    override lazy val tTagTO_0: ru.TypeTag[A1] = ru.typeTag[A1]

    @transient
    override lazy val tTagTO_1: ru.TypeTag[A2] = ru.typeTag[A2]

  }

  case class ActionA1A2ToA() extends Action2To1[A1, A2, A] with ActionBaseFields {

    override protected def execute(t1: A1, t2: A2)(context: ExecutionContext): A = ???

    @transient
    override lazy val tTagTI_0: ru.TypeTag[A1] = ru.typeTag[A1]

    @transient
    override lazy val tTagTO_0: ru.TypeTag[A] = ru.typeTag[A]

    @transient
    override lazy val tTagTI_1: ru.TypeTag[A2] = ru.typeTag[A2]

  }

  case class ActionAToALogging() extends Action1To1[A, A] with ActionBaseFields {

    logger.trace("Initializing logging to test the serialization")

    override protected def execute(t0: A)(context: ExecutionContext): A = ???

    def trace(message: String): Unit = logger.trace(message)

    @transient
    override lazy val tTagTI_0: ru.TypeTag[A] = ru.typeTag[A]

    @transient
    override lazy val tTagTO_0: ru.TypeTag[A] = ru.typeTag[A]

  }

}
