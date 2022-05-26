package ai.deepsense.deeplang.doperables

import spray.json.JsObject

import ai.deepsense.deeplang.DOperation
import ai.deepsense.deeplang.doperations.ConvertType
import ai.deepsense.deeplang.doperations.custom.Sink
import ai.deepsense.deeplang.doperations.custom.Source
import ai.deepsense.deeplang.params.custom.InnerWorkflow
import ai.deepsense.deeplang.params.custom.PublicParam
import ai.deepsense.deeplang.params.selections.MultipleColumnSelection
import ai.deepsense.deeplang.params.selections.NameColumnSelection
import ai.deepsense.graph.DeeplangGraph
import ai.deepsense.graph.Edge
import ai.deepsense.graph.Node
import ai.deepsense.models.json.graph.GraphJsonProtocol.GraphReader

object InnerWorkflowTestFactory {

  val sourceNodeId = "2603a7b5-aaa9-40ad-9598-23f234ec5c32"

  val sinkNodeId = "d7798d5e-b1c6-4027-873e-a6d653957418"

  val innerNodeId = "b22bd79e-337d-4223-b9ee-84c2526a1b75"

  val sourceNode = Node(sourceNodeId, Source())

  val sinkNode = Node(sinkNodeId, Sink())

  private def createInnerNodeOperation(targetType: TargetTypeChoice, graphReader: GraphReader): ConvertType = {

    val params = TypeConverter()
      .setTargetType(targetType)
      .setSelectedColumns(MultipleColumnSelection(Vector(NameColumnSelection(Set("column1")))))
      .paramValuesToJson
    new ConvertType().setParamsFromJson(params, graphReader)
  }

  private def createInnerNode(targetType: TargetTypeChoice, graphReader: GraphReader): Node[DOperation] =
    Node(innerNodeId, createInnerNodeOperation(targetType, graphReader))

  def simpleGraph(
      graphReader: GraphReader,
      targetType: TargetTypeChoice = TargetTypeChoices.StringTargetTypeChoice()
  ): DeeplangGraph = {
    val innerNode = createInnerNode(targetType, graphReader)
    DeeplangGraph(
      Set(sourceNode, sinkNode, innerNode),
      Set(Edge(sourceNode, 0, innerNode, 0), Edge(innerNode, 0, sinkNode, 0))
    )
  }

}
