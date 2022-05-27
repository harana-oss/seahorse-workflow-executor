package ai.deepsense.models.json.workflow.examples

import java.util.UUID

import spray.json._

import ai.deepsense.deeplang.Action
import ai.deepsense.graph.FlowGraph.FlowNode
import ai.deepsense.graph.FlowGraph
import ai.deepsense.graph.Edge
import ai.deepsense.graph.Node
import ai.deepsense.models.json.graph.GraphJsonProtocol.GraphReader
import ai.deepsense.models.json.workflow.WorkflowWithVariablesJsonProtocol
import ai.deepsense.models.workflows._

abstract class WorkflowCreator extends WorkflowWithVariablesJsonProtocol {

  val apiVersion: String = "0.4.0"

  protected def nodes: Seq[FlowNode]

  protected def edges: Seq[Edge]

  protected def experimentName: String

  protected def node(operation: Action): FlowNode = Node(UUID.randomUUID(), operation)

  override val graphReader: GraphReader = null

  def buildWorkflow(): WorkflowWithVariables = {
    val metadata                 = WorkflowMetadata(WorkflowType.Batch, apiVersion)
    val graph: FlowGraph     = FlowGraph(nodes.toSet, edges.toSet)
    val thirdPartyData: JsObject = JsObject()
    val variables: Variables     = Variables()
    val result                   =
      WorkflowWithVariables(Workflow.Id.randomId, metadata, graph, thirdPartyData, variables)
    // scalastyle:off println
    println(result.toJson.prettyPrint)
    // scalastyle:on println
    result
  }

}
