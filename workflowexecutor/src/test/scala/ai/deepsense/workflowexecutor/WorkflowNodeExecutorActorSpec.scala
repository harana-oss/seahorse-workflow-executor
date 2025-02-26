package ai.deepsense.workflowexecutor

import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import akka.testkit.TestActorRef
import akka.testkit.TestKit
import akka.testkit.TestProbe
import org.mockito.Mockito._
import org.mockito.ArgumentMatchers.{any, same}
import org.scalatest.concurrent.Eventually
import org.scalatestplus.mockito.MockitoSugar
import org.scalatest.BeforeAndAfter
import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import ai.deepsense.deeplang._
import ai.deepsense.deeplang.actionobjects.report.Report
import ai.deepsense.deeplang.inference.InferenceWarnings
import ai.deepsense.graph.FlowGraph.FlowNode
import ai.deepsense.graph.Node
import ai.deepsense.reportlib.model.ReportContent
import ai.deepsense.sparkutils.AkkaUtils
import ai.deepsense.workflowexecutor.WorkflowExecutorActor.Messages.NodeCompleted
import ai.deepsense.workflowexecutor.WorkflowExecutorActor.Messages.NodeFailed
import ai.deepsense.workflowexecutor.WorkflowExecutorActor.Messages.NodeStarted
import ai.deepsense.workflowexecutor.WorkflowNodeExecutorActor.Messages.Delete
import ai.deepsense.workflowexecutor.WorkflowNodeExecutorActor.Messages.Start

class WorkflowNodeExecutorActorSpec
    extends TestKit(ActorSystem("WorkflowNodeExecutorActorSpec"))
    with AnyWordSpecLike
    with Matchers
    with MockitoSugar
    with BeforeAndAfter
    with BeforeAndAfterAll
    with Eventually {

  override protected def afterAll(): Unit = AkkaUtils.terminate(system)

  "WorkflowNodeExecutorActor" when {
    "receives start" should {
      "infer knowledge and start execution of a node with correct parameters" in {
        val (probe, testedActor, node, operation, input) = fixutre()
        probe.send(testedActor, Start())
        probe.expectMsg(NodeStarted(node.id))

        eventually {
          verify(operation).inferKnowledgeUntyped(any())(any())
          verify(operation).executeUntyped(same(input))(any())
        }
      }
    }
    "its operation report type is set to metadata" should {
      "generate metadata report" in {
        val (probe, testedActor, node, result) = fixtureSucceedingOperation()
        node.value.setReportType(Action.ReportParam.Metadata())
        probe.send(testedActor, Start())
        probe.expectMsg(NodeStarted(node.id))
        val _ = probe.expectMsgType[NodeCompleted]
        verify(result(0), times(1)).report(extended = false)
        verify(result(1), times(1)).report(extended = false)
      }
    }
    "receives delete" should {
      "use delete DataFrame from storage" in {

        val node = mock[FlowNode]
        when(node.id).thenReturn(Node.Id.randomId)
        val dOperation = mockOperation
        when(node.value).thenReturn(dOperation)

        val removedExecutedContext = mock[ExecutionContext]
        val dataFrameStorage       = mock[ContextualDataFrameStorage]
        when(removedExecutedContext.dataFrameStorage).thenReturn(dataFrameStorage)

        val wnea: TestActorRef[WorkflowNodeExecutorActor] =
          TestActorRef(new WorkflowNodeExecutorActor(removedExecutedContext, node, Vector.empty))
        val probe = TestProbe()
        probe.send(wnea, Delete())

        eventually {
          verify(removedExecutedContext).dataFrameStorage
          verify(dataFrameStorage).removeNodeOutputDataFrames()
          verify(dataFrameStorage).removeNodeInputDataFrames()
        }
      }
    }
    "node completed" should {
      "respond NodeCompleted" in {
        val (probe, testedActor, node, output) = fixtureSucceedingOperation()
        probe.send(testedActor, Start())
        probe.expectMsg(NodeStarted(node.id))
        val nodeCompleted = probe.expectMsgType[NodeCompleted]
        nodeCompleted.id shouldBe node.id
        nodeCompleted.results.doperables.values should contain theSameElementsAs output
      }
    }
    "respond NodeFailed" when {
      "node failed" in {
        val (probe, testedActor, node, cause) = fixtureFailingOperation()
        probe.send(testedActor, Start())
        probe.expectMsg(NodeStarted(node.id))
        probe.expectMsgType[NodeFailed] shouldBe NodeFailed(node.id, cause)
      }
      "node failed with an Error" in {
        val (probe, testedActor, node, cause) = fixtureFailingOperationError()
        probe.send(testedActor, Start())
        probe.expectMsg(NodeStarted(node.id))
        val nodeFailed = probe.expectMsgType[NodeFailed]
        nodeFailed shouldBe a[NodeFailed]
        nodeFailed.id shouldBe node.id
        nodeFailed.cause.getCause shouldBe cause
      }
      "node's inference throws an exception" in {
        val (probe, testedActor, node, cause) = fixtureFailingInference()
        probe.send(testedActor, Start())
        probe.expectMsg(NodeStarted(node.id))
        probe.expectMsgType[NodeFailed] shouldBe NodeFailed(node.id, cause)
      }
    }
  }

  private def nodeExecutorActor(input: Vector[ActionObject], node: FlowNode): ActorRef =
    system.actorOf(Props(new WorkflowNodeExecutorActor(executionContext, node, input)))

  private def inferableOperable: ActionObject = {
    val operable = mock[ActionObject]
    operable
  }

  private def operableWithReports: ActionObject = {
    val operable = mock[ActionObject]
    val report   = mock[Report]
    when(report.content).thenReturn(mock[ReportContent])
    when(operable.report(extended = false)).thenReturn(report)
    operable
  }

  private def mockOperation: Action = {
    val dOperation = mock[Action]
    when(dOperation.name).thenReturn("mockedName")
    dOperation
  }

  private def fixtureFailingInference(): (TestProbe, ActorRef, FlowNode, NullPointerException) = {
    val operation = mockOperation
    val cause     = new NullPointerException("test exception")
    when(operation.inferKnowledgeUntyped(any())(any()))
      .thenThrow(cause)
    val (probe, testedActor, node, _, _) = fixtureWithOperation(operation)
    (probe, testedActor, node, cause)
  }

  private def fixtureFailingOperation(): (TestProbe, ActorRef, FlowNode, NullPointerException) = {
    val operation = mockOperation
    val cause     = new NullPointerException("test exception")
    when(operation.executeUntyped(any[Vector[ActionObject]]())(any[ExecutionContext]()))
      .thenThrow(cause)
    val (probe, testedActor, node, _, _) = fixtureWithOperation(operation)
    (probe, testedActor, node, cause)
  }

  private def fixtureFailingOperationError(): (TestProbe, ActorRef, FlowNode, Throwable) = {
    val operation = mockOperation
    val cause     = new AssertionError("test exception")
    when(operation.executeUntyped(any[Vector[ActionObject]]())(any[ExecutionContext]()))
      .thenThrow(cause)
    val (probe, testedActor, node, _, _) = fixtureWithOperation(operation)
    (probe, testedActor, node, cause)
  }

  private def fixtureSucceedingOperation(): (TestProbe, ActorRef, FlowNode, Vector[ActionObject]) = {
    val operation = mockOperation
    val output    = Vector(operableWithReports, operableWithReports)
    when(operation.executeUntyped(any())(any()))
      .thenReturn(output)
    val (probe, testedActor, node, doperation, _) = fixtureWithOperation(operation)
    (probe, testedActor, node, output)
  }

  private def fixtureWithOperation(
      dOperation: Action
  ): (TestProbe, ActorRef, FlowNode, Action, Vector[ActionObject]) = {
    val node = mock[FlowNode]
    when(node.id).thenReturn(Node.Id.randomId)
    when(node.value).thenReturn(dOperation)
    val probe       = TestProbe()
    val input       = Vector(inferableOperable, inferableOperable)
    val testedActor = nodeExecutorActor(input, node)
    (probe, testedActor, node, dOperation, input)
  }

  private def fixutre(): (TestProbe, ActorRef, FlowNode, Action, Vector[ActionObject]) = {
    val dOperation = mockOperation
    when(dOperation.inferKnowledgeUntyped(any())(any()))
      .thenReturn((Vector[Knowledge[ActionObject]](), mock[InferenceWarnings]))
    when(dOperation.executeUntyped(any())(any()))
      .thenReturn(Vector())
    fixtureWithOperation(dOperation)
  }

  val executionContext = LocalExecutionContext.createExecutionContext()

}
