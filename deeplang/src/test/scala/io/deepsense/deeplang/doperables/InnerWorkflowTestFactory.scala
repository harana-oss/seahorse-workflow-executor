package io.deepsense.deeplang.doperables

import spray.json.JsObject

import io.deepsense.deeplang.DOperation
import io.deepsense.deeplang.doperations.ConvertType
import io.deepsense.deeplang.doperations.custom.Sink
import io.deepsense.deeplang.doperations.custom.Source
import io.deepsense.deeplang.params.custom.InnerWorkflow
import io.deepsense.deeplang.params.custom.PublicParam
import io.deepsense.deeplang.params.selections.MultipleColumnSelection
import io.deepsense.deeplang.params.selections.NameColumnSelection
import io.deepsense.graph.DeeplangGraph
import io.deepsense.graph.Edge
import io.deepsense.graph.Node

object InnerWorkflowTestFactory {

  val sourceNodeId = "2603a7b5-aaa9-40ad-9598-23f234ec5c32"

  val sinkNodeId = "d7798d5e-b1c6-4027-873e-a6d653957418"

  val innerNodeId = "b22bd79e-337d-4223-b9ee-84c2526a1b75"

  val sourceNode = Node(sourceNodeId, Source())

  val sinkNode = Node(sinkNodeId, Sink())

  private def createInnerNodeOperation(targetType: TargetTypeChoice): ConvertType = {
    val params = TypeConverter()
      .setTargetType(targetType)
      .setSelectedColumns(MultipleColumnSelection(Vector(NameColumnSelection(Set("column1")))))
      .paramValuesToJson
    new ConvertType().setParamsFromJson(params)
  }

  private def createInnerNode(targetType: TargetTypeChoice): Node[DOperation] =
    Node(innerNodeId, createInnerNodeOperation(targetType))

  def simpleGraph(targetType: TargetTypeChoice = TargetTypeChoices.StringTargetTypeChoice()): DeeplangGraph = {
    val innerNode = createInnerNode(targetType)
    DeeplangGraph(
      Set(sourceNode, sinkNode, innerNode),
      Set(Edge(sourceNode, 0, innerNode, 0), Edge(innerNode, 0, sinkNode, 0))
    )
  }

  def simpleInnerWorkflow(publicParams: List[PublicParam] = List.empty): InnerWorkflow =
    InnerWorkflow(simpleGraph(), JsObject(), publicParams)

}
