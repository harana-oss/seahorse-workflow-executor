package io.deepsense.graph

import scala.reflect.runtime.{universe => ru}

import org.scalatestplus.mockito.MockitoSugar

import io.deepsense.commons.utils.Version
import io.deepsense.deeplang._
import io.deepsense.deeplang.doperables.DOperableMock
import io.deepsense.graph.DeeplangGraph.DeeplangNode

object RandomNodeFactory {

  def randomNode(operation: DOperation): DeeplangNode = Node(Node.Id.randomId, operation)

}

object DClassesForDOperations extends MockitoSugar {

  trait A extends DOperableMock

  case class A1() extends A

  case class A2() extends A

}

object DOperationTestClasses {

  import io.deepsense.graph.DClassesForDOperations._

  trait DOperationBaseFields extends DOperation {

    // NOTE: id will be different for each instance
    override val id: DOperation.Id = DOperation.Id.randomId

    override val name: String = ""

    override val description: String = ""

    val params: Array[io.deepsense.deeplang.params.Param[_]] = Array()

  }

  case class DOperationCreateA1() extends DOperation0To1[A1] with DOperationBaseFields {

    override protected def execute()(context: ExecutionContext): A1 = ???

    @transient
    override lazy val tTagTO_0: ru.TypeTag[A1] = ru.typeTag[A1]

  }

  case class DOperationReceiveA1() extends DOperation1To0[A1] with DOperationBaseFields {

    override protected def execute(t0: A1)(context: ExecutionContext): Unit = ???

    @transient
    override lazy val tTagTI_0: ru.TypeTag[A1] = ru.typeTag[A1]

  }

  case class DOperationA1ToA() extends DOperation1To1[A1, A] with DOperationBaseFields {

    override protected def execute(t1: A1)(context: ExecutionContext): A = ???

    @transient
    override lazy val tTagTI_0: ru.TypeTag[A1] = ru.typeTag[A1]

    @transient
    override lazy val tTagTO_0: ru.TypeTag[A] = ru.typeTag[A]

  }

  case class DOperationAToA1A2() extends DOperation1To2[A, A1, A2] with DOperationBaseFields {

    override protected def execute(in: A)(context: ExecutionContext): (A1, A2) = ???

    @transient
    override lazy val tTagTI_0: ru.TypeTag[A] = ru.typeTag[A]

    @transient
    override lazy val tTagTO_0: ru.TypeTag[A1] = ru.typeTag[A1]

    @transient
    override lazy val tTagTO_1: ru.TypeTag[A2] = ru.typeTag[A2]

  }

  case class DOperationA1A2ToA() extends DOperation2To1[A1, A2, A] with DOperationBaseFields {

    override protected def execute(t1: A1, t2: A2)(context: ExecutionContext): A = ???

    @transient
    override lazy val tTagTI_0: ru.TypeTag[A1] = ru.typeTag[A1]

    @transient
    override lazy val tTagTO_0: ru.TypeTag[A] = ru.typeTag[A]

    @transient
    override lazy val tTagTI_1: ru.TypeTag[A2] = ru.typeTag[A2]

  }

  case class DOperationAToALogging() extends DOperation1To1[A, A] with DOperationBaseFields {

    logger.trace("Initializing logging to test the serialization")

    override protected def execute(t0: A)(context: ExecutionContext): A = ???

    def trace(message: String): Unit = logger.trace(message)

    @transient
    override lazy val tTagTI_0: ru.TypeTag[A] = ru.typeTag[A]

    @transient
    override lazy val tTagTO_0: ru.TypeTag[A] = ru.typeTag[A]

  }

}
