package ai.deepsense.workflowexecutor

import scala.util.control.NonFatal

import akka.actor.Actor
import akka.actor.PoisonPill
import ai.deepsense.commons.models.Entity
import ai.deepsense.commons.utils.Logging
import ai.deepsense.deeplang._
import ai.deepsense.deeplang.actionobjects.dataframe.DataFrame
import ai.deepsense.graph.FlowGraph.FlowNode
import ai.deepsense.reportlib.model.ReportContent
import ai.deepsense.reportlib.model.ReportType
import ai.deepsense.workflowexecutor.WorkflowExecutorActor.Messages.NodeCompleted
import ai.deepsense.workflowexecutor.WorkflowExecutorActor.Messages.NodeFailed
import ai.deepsense.workflowexecutor.WorkflowExecutorActor.Messages.NodeStarted

/** WorkflowNodeExecutorActor is responsible for execution of single node. Sends NodeStarted at the beginning and
  * NodeCompleted or NodeFailed at the end of execution depending on whether the execution succeeded or failed.
  */
class WorkflowNodeExecutorActor(executionContext: ExecutionContext, node: FlowNode, input: Vector[ActionObject])
    extends Actor
    with Logging {

  import ai.deepsense.workflowexecutor.WorkflowNodeExecutorActor.Messages._

  val nodeDescription      = s"'${node.value.name}-${node.id}'"

  var executionStart: Long = _

  override def receive: Receive = {
    case Start() =>
      executionStart = System.currentTimeMillis()
      logger.info(s"Starting execution of node $nodeDescription")
      sendStarted()
      try {
        asSparkJobGroup {
          val resultVector         = executeOperation()
          val nodeExecutionResults = nodeExecutionResultsFrom(resultVector)
          sendCompleted(nodeExecutionResults)
        }
      } catch {
        case SparkExceptionAsDeeplangException(deeplangEx) => sendFailed(deeplangEx)
        case e: Exception                                  => sendFailed(e)
        case NonFatal(e)                                   => sendFailed(new RuntimeException(e))
        case fatal: Throwable =>
          logger.error(s"FATAL ERROR. MSG: ${fatal.getMessage}", fatal)
          fatal.printStackTrace()
          throw fatal
      } finally {
        // Exception thrown here could result in slightly delayed graph execution
        val duration = (System.currentTimeMillis() - executionStart) / 1000.0
        logger.info(s"Ending execution of node $nodeDescription (duration: $duration seconds)")
        self ! PoisonPill
      }
    case Delete() =>
      val storage = executionContext.dataFrameStorage
      storage.removeNodeOutputDataFrames()
      storage.removeNodeInputDataFrames()
  }

  def sendFailed(e: Exception): Unit = {
    logger.error(s"Workflow execution failed in node with id=${node.id}.", e)
    sender ! NodeFailed(node.id, e)
  }

  def sendCompleted(nodeExecutionResults: NodeExecutionResults): Unit = {
    val nodeCompleted = NodeCompleted(node.id, nodeExecutionResults)
    logger.debug(s"Node completed: ${nodeExecutionResults.doperables}")
    sender ! nodeCompleted
  }

  def sendStarted(): Unit = {
    val nodeStarted = NodeStarted(node.id)
    sender ! nodeStarted
    logger.debug(s"Sending $nodeStarted.")
  }

  def nodeExecutionResultsFrom(operationResults: Vector[ActionObject]): NodeExecutionResults = {
    val registeredResults: Seq[(Entity.Id, ActionObject)]  = registerResults(operationResults)
    val registeredResultsMap: Map[Entity.Id, ActionObject] = registeredResults.toMap
    val reports: Map[Entity.Id, ReportContent]          = collectReports(registeredResultsMap)
    NodeExecutionResults(registeredResults.map(_._1), reports, registeredResultsMap)
  }

  private def registerResults(operables: Seq[ActionObject]): Seq[(Entity.Id, ActionObject)] = {
    logger.debug(s"Registering data from operation output ports in node ${node.id}")
    val results: Seq[(Entity.Id, ActionObject)] = operables.map(dOperable => (Entity.Id.randomId, dOperable))
    logger.debug(s"Data registered for $nodeDescription: results=$results")
    results
  }

  def collectReports(results: Map[Entity.Id, ActionObject]): Map[Entity.Id, ReportContent] = {
    logger.debug(s"Collecting reports for ${node.id}")
    results.map { case (id, dOperable) =>
      (id, dOperable.report(extended = node.value.getReportType == Action.ReportParam.Extended()).content)
    }
  }

  def executeOperation(): Vector[ActionObject] = {
    logger.debug(s"$nodeDescription inputVector.size = ${input.size}")
    val inputKnowledge = input.map(dOperable => Knowledge(dOperable))
    // if inference throws, we do not perform execution
    node.value.inferKnowledgeUntyped(inputKnowledge)(executionContext.inferContext)

    val resultVector = node.value.executeUntyped(input)(executionContext)

    resultVector.zipWithIndex.foreach {
      case (dataFrame: DataFrame, portNumber: Int) =>
        executionContext.dataFrameStorage.setOutputDataFrame(portNumber, dataFrame.sparkDataFrame)
      case (_, _) => ()
    }

    logger.debug(s"$nodeDescription executed (without reports): $resultVector")
    resultVector
  }

  private def asSparkJobGroup[T](sparkCode: => T): T = try {
    // interrupt on cancel = false because of HDFS-1208
    executionContext.sparkContext.setJobGroup(
      JobGroupIdForNode(node),
      s"Execution of node id=${node.id.toString}",
      interruptOnCancel = false
    )
    sparkCode
  } finally
    // clear job group, because this thread will be used by other actors
    executionContext.sparkContext.clearJobGroup()

}

object WorkflowNodeExecutorActor {

  object Messages {

    sealed trait Message

    case class Start()  extends Message

    case class Delete() extends Message

  }

}
