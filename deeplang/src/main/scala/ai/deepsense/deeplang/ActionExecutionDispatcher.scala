package ai.deepsense.deeplang

import scala.collection.concurrent.TrieMap
import scala.concurrent.Future
import scala.concurrent.Promise

import ai.deepsense.commons.models.Id

class ActionExecutionDispatcher {

  import ActionExecutionDispatcher._

  private val operationEndPromises: TrieMap[OperationId, Promise[Result]] = TrieMap.empty

  def executionStarted(workflowId: Id, nodeId: Id): Future[Result] = {
    val promise: Promise[Result] = Promise()
    require(operationEndPromises.put((workflowId, nodeId), promise).isEmpty)
    promise.future
  }

  def executionEnded(workflowId: Id, nodeId: Id, result: Result): Unit = {
    val promise = operationEndPromises.remove((workflowId, nodeId))
    require(promise.isDefined)
    promise.get.success(result)
  }

}

object ActionExecutionDispatcher {

  type OperationId = (Id, Id)

  type Error = String

  type Result = Either[Error, Unit]

}
