package ai.deepsense.deeplang.doperations

import spray.json.JsObject

import ai.deepsense.deeplang._
import ai.deepsense.deeplang.doperables.CustomTransformer
import ai.deepsense.deeplang.doperables.TargetTypeChoices
import ai.deepsense.deeplang.doperables.TypeConverter
import ai.deepsense.deeplang.doperations.custom.Sink
import ai.deepsense.deeplang.doperations.custom.Source
import ai.deepsense.deeplang.inference.InferContext
import ai.deepsense.deeplang.params.ParameterType
import ai.deepsense.deeplang.params.custom.InnerWorkflow
import ai.deepsense.deeplang.params.custom.PublicParam
import ai.deepsense.deeplang.params.selections.MultipleColumnSelection
import ai.deepsense.deeplang.params.selections.NameColumnSelection
import ai.deepsense.graph.DeeplangGraph
import ai.deepsense.graph.Edge
import ai.deepsense.graph.Node
import ai.deepsense.models.json.graph.GraphJsonProtocol.GraphReader

class CreateCustomTransformerSpec extends UnitSpec {

  val node1Id = Node.Id.randomId

  val node2Id = Node.Id.randomId

  object MockCreateCustomTransformer extends CreateCustomTransformer {

    def createWithParam: CreateCustomTransformer = {
      set(
        innerWorkflow,
        createInnerWorkflow(
          PublicParam(node1Id, "target type", "public param 1"),
          PublicParam(node2Id, "target type", "public param 2")
        )
      )
    }

  }

  "CreateCustomTransformer" should {
    "create CustomTransformer with public params" in {

      val operation        = MockCreateCustomTransformer.createWithParam
      val executionContext = mock[ExecutionContext]

      val results = operation.executeUntyped(Vector.empty)(executionContext)
      results.length shouldBe 1
      results(0) shouldBe a[CustomTransformer]
      val result  = results(0).asInstanceOf[CustomTransformer]

      result.params.length shouldBe 2

      result.params(0).name shouldBe "public param 1"
      result.params(0).parameterType shouldBe ParameterType.Choice

      result.params(1).name shouldBe "public param 2"
      result.params(1).parameterType shouldBe ParameterType.Choice
    }

    "create CustomTransformer without public params" in {
      val operation        = CreateCustomTransformer()
      val executionContext = mock[ExecutionContext]

      val results = operation.executeUntyped(Vector.empty)(executionContext)
      results.size shouldBe 1
      results(0) shouldBe a[CustomTransformer]
      val result  = results(0).asInstanceOf[CustomTransformer]

      result.params.length shouldBe 0
    }

    "infer parameters of CustomTransformer from input inner workflow" in {
      val operation    = MockCreateCustomTransformer.createWithParam
      val inferContext = mock[InferContext]

      val results = operation.inferKnowledgeUntyped(Vector.empty)(inferContext)._1.map(_.single)
      results.length shouldBe 1
      results(0) shouldBe a[CustomTransformer]
      val result  = results(0).asInstanceOf[CustomTransformer]

      result.params.length shouldBe 2

      result.params(0).name shouldBe "public param 1"
      result.params(0).parameterType shouldBe ParameterType.Choice

      result.params(1).name shouldBe "public param 2"
      result.params(1).parameterType shouldBe ParameterType.Choice
    }
  }

  private def createInnerWorkflow(publicParams: PublicParam*): InnerWorkflow = {
    val graphReader  = mock[GraphReader]
    val sourceNodeId = "2603a7b5-aaa9-40ad-9598-23f234ec5c32"
    val sinkNodeId   = "d7798d5e-b1c6-4027-873e-a6d653957418"

    val sourceNode = Node(sourceNodeId, Source())
    val sinkNode   = Node(sinkNodeId, Sink())

    val node1Operation = {
      val params = TypeConverter()
        .setTargetType(TargetTypeChoices.StringTargetTypeChoice())
        .setSelectedColumns(MultipleColumnSelection(Vector(NameColumnSelection(Set("column1")))))
        .paramValuesToJson
      new ConvertType().setParamsFromJson(params, graphReader)
    }

    val node2Operation = {
      val params = TypeConverter()
        .setTargetType(TargetTypeChoices.StringTargetTypeChoice())
        .setSelectedColumns(MultipleColumnSelection(Vector(NameColumnSelection(Set("column1")))))
        .paramValuesToJson
      new ConvertType().setParamsFromJson(params, graphReader)
    }

    val node1 = Node(node1Id, node1Operation)
    val node2 = Node(node2Id, node2Operation)

    val simpleGraph = DeeplangGraph(
      Set(sourceNode, sinkNode, node1, node2),
      Set(Edge(sourceNode, 0, node1, 0), Edge(node1, 0, node2, 0), Edge(node2, 0, sinkNode, 0))
    )

    InnerWorkflow(simpleGraph, JsObject(), publicParams.toList)
  }

}
