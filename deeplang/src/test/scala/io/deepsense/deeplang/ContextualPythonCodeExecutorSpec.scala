package io.deepsense.deeplang

import scala.concurrent.Future

import org.mockito.Mockito._
import org.scalatest.BeforeAndAfter

import io.deepsense.commons.models.Id

class ContextualPythonCodeExecutorSpec extends UnitSpec with BeforeAndAfter {

  val workflowId = Id.randomId
  val nodeId = Id.randomId

  val code = "some code"

  val pythonCodeExecutor = mock[CustomCodeExecutor]
  val operationExecutionDispatcher = mock[OperationExecutionDispatcher]
  val customCodeExecutionProvider = mock[CustomCodeExecutionProvider]

  var executor: ContextualCustomCodeExecutor = _

  before {
    executor = new ContextualCustomCodeExecutor(customCodeExecutionProvider, workflowId, nodeId)
    when(customCodeExecutionProvider.pythonCodeExecutor)
      .thenReturn(pythonCodeExecutor)
    when(customCodeExecutionProvider.operationExecutionDispatcher)
      .thenReturn(operationExecutionDispatcher)
  }

  "ContextualPythonCodeExecutor" should {

    "validate code" in {
      when(pythonCodeExecutor.isValid(code)).thenReturn(true)

      executor.isPythonValid(code) shouldBe true
    }

    "execute code" in {
      when(operationExecutionDispatcher.executionStarted(workflowId, nodeId))
        .thenReturn(Future.successful(Right(())))
      doNothing().when(pythonCodeExecutor).run(workflowId.toString, nodeId.toString, code)

      executor.runPython(code)
    }
  }
}
