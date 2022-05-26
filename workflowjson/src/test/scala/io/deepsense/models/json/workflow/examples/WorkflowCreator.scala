package io.deepsense.models.json.workflow.examples

import java.util.UUID

import spray.json._

import io.deepsense.deeplang.DOperation
import io.deepsense.graph.DeeplangGraph.DeeplangNode
import io.deepsense.graph.DeeplangGraph
import io.deepsense.graph.Edge
import io.deepsense.graph.Node
import io.deepsense.models.json.graph.GraphJsonProtocol.GraphReader
import io.deepsense.models.json.workflow.WorkflowWithVariablesJsonProtocol
import io.deepsense.models.workflows._

abstract class WorkflowCreator extends WorkflowWithVariablesJsonProtocol {

  val apiVersion: String = "0.4.0"

  protected def nodes: Seq[DeeplangNode]

  protected def edges: Seq[Edge]

  protected def experimentName: String

  protected def node(operation: DOperation): DeeplangNode = Node(UUID.randomUUID(), operation)

  override val graphReader: GraphReader = null

  def buildWorkflow(): WorkflowWithVariables = {
    val metadata                 = WorkflowMetadata(WorkflowType.Batch, apiVersion)
    val graph: DeeplangGraph     = DeeplangGraph(nodes.toSet, edges.toSet)
    val thirdPartyData: JsObject = JsObject()
    val variables: Variables     = Variables()
    val result =
      WorkflowWithVariables(Workflow.Id.randomId, metadata, graph, thirdPartyData, variables)
    // scalastyle:off println
    println(result.toJson.prettyPrint)
    // scalastyle:on println
    result
  }

}
