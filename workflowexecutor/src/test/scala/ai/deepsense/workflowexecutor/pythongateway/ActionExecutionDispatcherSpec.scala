package ai.deepsense.workflowexecutor.pythongateway

import org.scalatest.BeforeAndAfter
import org.scalatestplus.mockito.MockitoSugar
import ai.deepsense.commons.StandardSpec
import ai.deepsense.commons.models.Id
import ai.deepsense.deeplang.ActionExecutionDispatcher

class ActionExecutionDispatcherSpec extends StandardSpec with MockitoSugar with BeforeAndAfter {

  val workflowId = Id.randomId

  val nodeId     = Id.randomId

  var dispatcher: ActionExecutionDispatcher = _

  before {
    dispatcher = new ActionExecutionDispatcher
  }

  "OperationExecutionDispatcher" should {

    "execute operation and finish" when {

      "notified of success with proper workflow and node id" in {
        val future = dispatcher.executionStarted(workflowId, nodeId)
        future.isCompleted shouldBe false

        dispatcher.executionEnded(workflowId, nodeId, Right(()))
        future.isCompleted shouldBe true
      }

      "notified of failure with proper workflow and node id" in {
        val future = dispatcher.executionStarted(workflowId, nodeId)
        future.isCompleted shouldBe false

        dispatcher.executionEnded(workflowId, nodeId, Left("A stacktrace"))
        future.isCompleted shouldBe true
        future.value.get.get shouldBe Left("A stacktrace")
      }
    }

    "throw an exception" when {

      "multiple executions of the same node are started" in {
        dispatcher.executionStarted(workflowId, nodeId)

        an[IllegalArgumentException] shouldBe thrownBy {
          dispatcher.executionStarted(workflowId, nodeId)
        }
      }

      "notified with non-existing workflow id" in {
        val future = dispatcher.executionStarted(workflowId, nodeId)
        future.isCompleted shouldBe false

        an[IllegalArgumentException] shouldBe thrownBy {
          dispatcher.executionEnded(Id.randomId, nodeId, Right(()))
        }
      }

      "notified with non-existing node id" in {
        val future = dispatcher.executionStarted(workflowId, nodeId)
        future.isCompleted shouldBe false

        an[IllegalArgumentException] shouldBe thrownBy {
          dispatcher.executionEnded(workflowId, Id.randomId, Right(()))
        }
      }
    }
  }

}
