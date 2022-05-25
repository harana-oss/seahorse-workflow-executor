package io.deepsense.workflowexecutor.executor

import spray.json._

import io.deepsense.commons.models.Entity
import io.deepsense.deeplang._
import io.deepsense.deeplang.doperables.dataframe.DataFrame
import io.deepsense.deeplang.doperations.exceptions.CustomOperationExecutionException
import io.deepsense.deeplang.params.custom.InnerWorkflow
import io.deepsense.graph.DeeplangGraph.DeeplangNode
import io.deepsense.graph.Node
import io.deepsense.models.json.graph.GraphJsonProtocol.GraphReader
import io.deepsense.models.json.workflow.InnerWorkflowJsonProtocol
import io.deepsense.models.workflows._
import io.deepsense.workflowexecutor.NodeExecutionResults
import io.deepsense.workflowexecutor.buildinfo.BuildInfo
import io.deepsense.workflowexecutor.partialexecution._

class InnerWorkflowExecutorImpl(override val graphReader: GraphReader)
  extends InnerWorkflowExecutor
  with InnerWorkflowJsonProtocol {

  override def parse(workflow: JsObject): InnerWorkflow = {
    workflow.convertTo[InnerWorkflow]
  }

  override def toJson(innerWorkflow: InnerWorkflow): JsObject = {
    innerWorkflow.toJson.asJsObject
  }

  override def execute(
      executionContext: CommonExecutionContext,
      innerWorkflow: InnerWorkflow,
      dataFrame: DataFrame): DataFrame = {

    val workflowId = Workflow.Id.randomId

    val workflowWithResults = WorkflowWithResults(
      workflowId,
      WorkflowMetadata(WorkflowType.Batch, BuildInfo.version),
      innerWorkflow.graph,
      innerWorkflow.thirdPartyData,
      ExecutionReport(Map()),
      WorkflowInfo.forId(workflowId)
    )
    val statefulWorkflow = StatefulWorkflow(
      executionContext, workflowWithResults, Execution.defaultExecutionFactory)

    val nodesToExecute = statefulWorkflow.currentExecution.graph.nodes.map(_.id)
    statefulWorkflow.launch(nodesToExecute)

    statefulWorkflow.currentExecution.executionReport.error.map { e =>
      throw CustomOperationExecutionException(
        e.title + "\n" + e.message.getOrElse("") + "\n" + e.details.values.mkString("\n"))
    }

    statefulWorkflow.nodeStarted(innerWorkflow.source.id)

    nodeCompleted(statefulWorkflow,
      innerWorkflow.source.id, nodeExecutionResultsFrom(Vector(dataFrame)))

    run(statefulWorkflow, executionContext)

    val (_, result) =
      statefulWorkflow.currentExecution.graph.states(innerWorkflow.sink.id).dOperables.head
    result.asInstanceOf[DataFrame]
  }

  private def run(
      statefulWorkflow: StatefulWorkflow, executionContext: CommonExecutionContext): Unit = {
    statefulWorkflow.currentExecution match {
      case running: RunningExecution =>
        val readyNodes: Seq[ReadyNode] = statefulWorkflow.startReadyNodes()
        readyNodes.foreach { readyNode =>
          val input = readyNode.input.toVector
          val nodeExecutionContext = executionContext.createExecutionContext(
            statefulWorkflow.workflowId, readyNode.node.id)
          val results = executeOperation(readyNode.node, input, nodeExecutionContext)
          val nodeResults = nodeExecutionResultsFrom(results)
          nodeCompleted(statefulWorkflow, readyNode.node.id, nodeResults)
        }
        run(statefulWorkflow, executionContext)
      case _ => ()
    }
  }

  private def executeOperation(
      node: DeeplangNode,
      input: Vector[DOperable],
      executionContext: ExecutionContext): Vector[DOperable] = {
    val inputKnowledge = input.map { dOperable => DKnowledge(dOperable) }
    node.value.inferKnowledgeUntyped(inputKnowledge)(executionContext.inferContext)
    node.value.executeUntyped(input)(executionContext)
  }

  private def nodeExecutionResultsFrom(
      operationResults: Vector[DOperable]): NodeExecutionResults = {
    val results = operationResults.map { dOperable => (Entity.Id.randomId, dOperable) }
    NodeExecutionResults(results.map(_._1), Map(), results.toMap)
  }

  private def nodeCompleted(
      statefulWorkflow: StatefulWorkflow,
      id: Node.Id,
      nodeExecutionResults: NodeExecutionResults): Unit = {
    statefulWorkflow.nodeFinished(
      id,
      nodeExecutionResults.entitiesId,
      nodeExecutionResults.reports,
      nodeExecutionResults.doperables)
  }
}
