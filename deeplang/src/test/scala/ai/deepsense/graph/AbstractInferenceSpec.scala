package ai.deepsense.graph

import scala.reflect.runtime.{universe => ru}

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import ai.deepsense.deeplang._
import ai.deepsense.deeplang.catalogs.actionobjects.ActionObjectCatalog
import ai.deepsense.deeplang.exceptions.FlowException
import ai.deepsense.deeplang.exceptions.FlowMultiException
import ai.deepsense.deeplang.inference.InferContext
import ai.deepsense.deeplang.inference.InferenceWarning
import ai.deepsense.deeplang.inference.InferenceWarnings
import ai.deepsense.deeplang.parameters.exceptions.ValidationException
import ai.deepsense.graph.DClassesForActions._
import ai.deepsense.graph.ActionTestClasses._
import ai.deepsense.graph.FlowGraph.FlowNode

class AbstractInferenceSpec extends AnyWordSpec with DeeplangTestSupport with Matchers {

  val hierarchy = new ActionObjectCatalog

  hierarchy.registerActionObject[A1]()
  hierarchy.registerActionObject[A2]()

  val knowledgeA1: Knowledge[ActionObject] = Knowledge(A1())

  val knowledgeA2: Knowledge[ActionObject] = Knowledge(A2())

  val knowledgeA12: Knowledge[ActionObject] = Knowledge(A1(), A2())

  val inferenceCtx: InferContext = createInferContext(hierarchy)

  /** This operation can be set to:
    *   - have invalid parameters
    *   - throw inference errors By default it infers A1 on its output port.
    */
  case class ActionA1A2ToFirst() extends Action2To1[A1, A2, A] with ActionBaseFields {

    import ActionA1A2ToFirst._

    override protected def execute(t1: A1, t2: A2)(context: ExecutionContext): A = ???

    override def validateParams: Vector[FlowException] =
      if (paramsValid) Vector.empty else Vector(parameterInvalidError)

    private var paramsValid: Boolean = _

    def setParamsValid(): Unit = paramsValid = true

    def setParamsInvalid(): Unit = paramsValid = false

    private var inferenceShouldThrow = false

    private var multiException = false

    def setInferenceErrorThrowing(): Unit = inferenceShouldThrow = true

    def setInferenceErrorThrowingMultiException(): Unit = {
      inferenceShouldThrow = true
      multiException = true
    }

    override protected def inferKnowledge(k0: Knowledge[A1], k1: Knowledge[A2])(
        context: InferContext
    ): (Knowledge[A], InferenceWarnings) = {
      if (inferenceShouldThrow) {
        if (multiException)
          throw multiInferenceError
        else
          throw inferenceError
      }
      (k0, InferenceWarnings(warning))
    }

    override lazy val tTagTI_0: ru.TypeTag[A1] = ru.typeTag[A1]

    override lazy val tTagTO_0: ru.TypeTag[A] = ru.typeTag[A]

    override lazy val tTagTI_1: ru.TypeTag[A2] = ru.typeTag[A2]

  }

  object ActionA1A2ToFirst {

    val parameterInvalidError = new ValidationException("") {}

    val inferenceError = new FlowException("") {}

    val multiInferenceError = FlowMultiException(Vector(mock[FlowException], mock[FlowException]))

    val warning = mock[InferenceWarning]

  }

  val idCreateA1 = Node.Id.randomId

  val idA1ToA = Node.Id.randomId

  val idAToA1A2 = Node.Id.randomId

  val idA1A2ToFirst = Node.Id.randomId

  protected def nodeCreateA1 = Node(idCreateA1, ActionCreateA1())

  protected def nodeA1ToA = Node(idA1ToA, ActionA1ToA())

  protected def nodeAToA1A2 = Node(idAToA1A2, ActionAToA1A2())

  protected def nodeA1A2ToFirst = Node(idA1A2ToFirst, ActionA1A2ToFirst())

  def validGraph: FlowGraph = FlowGraph(
    nodes = Set(nodeCreateA1, nodeAToA1A2, nodeA1A2ToFirst),
    edges = Set(
      Edge(nodeCreateA1, 0, nodeAToA1A2, 0),
      Edge(nodeAToA1A2, 0, nodeA1A2ToFirst, 0),
      Edge(nodeAToA1A2, 1, nodeA1A2ToFirst, 1)
    )
  )

  def setParametersValid(node: FlowNode): Unit =
    node.value.asInstanceOf[ActionA1A2ToFirst].setParamsValid()

  def setInferenceErrorThrowing(node: FlowNode): Unit =
    node.value.asInstanceOf[ActionA1A2ToFirst].setInferenceErrorThrowing()

  def setInferenceErrorMultiThrowing(node: FlowNode): Unit =
    node.value.asInstanceOf[ActionA1A2ToFirst].setInferenceErrorThrowingMultiException()

  def setParametersInvalid(node: FlowNode): Unit =
    node.value.asInstanceOf[ActionA1A2ToFirst].setParamsInvalid()

  def setParametersValid(graph: FlowGraph): Unit = setInGraph(graph, _.setParamsValid())

  def setInGraph(graph: FlowGraph, f: ActionA1A2ToFirst => Unit): Unit = {
    val node = graph.node(idA1A2ToFirst)
    f(node.value.asInstanceOf[ActionA1A2ToFirst])
  }

}
