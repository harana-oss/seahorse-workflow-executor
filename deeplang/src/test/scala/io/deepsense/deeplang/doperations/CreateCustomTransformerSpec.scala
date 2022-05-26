package io.deepsense.deeplang.doperations

import org.mockito.ArgumentMatchers._
import org.mockito.Mockito._
import spray.json.JsObject

import io.deepsense.deeplang._
import io.deepsense.deeplang.doperables.CustomTransformer
import io.deepsense.deeplang.doperables.TargetTypeChoices
import io.deepsense.deeplang.doperables.TypeConverter
import io.deepsense.deeplang.doperations.custom.Sink
import io.deepsense.deeplang.doperations.custom.Source
import io.deepsense.deeplang.inference.InferContext
import io.deepsense.deeplang.params.ParameterType
import io.deepsense.deeplang.params.custom.InnerWorkflow
import io.deepsense.deeplang.params.custom.PublicParam
import io.deepsense.deeplang.params.selections.MultipleColumnSelection
import io.deepsense.deeplang.params.selections.NameColumnSelection
import io.deepsense.graph.DeeplangGraph
import io.deepsense.graph.Edge
import io.deepsense.graph.Node

class CreateCustomTransformerSpec extends UnitSpec {

  val node1Id = Node.Id.randomId

  val node2Id = Node.Id.randomId

  "CreateCustomTransformer" should {

    "create CustomTransformer with public params" in {
      val operation = CreateCustomTransformer()
      val executionContext = createExecutionContext(
        createInnerWorkflow(
          PublicParam(node1Id, "target type", "public param 1"),
          PublicParam(node2Id, "target type", "public param 2")
        )
      )

      val results = operation.executeUntyped(Vector.empty)(executionContext)
      results.length shouldBe 1
      results(0) shouldBe a[CustomTransformer]
      val result = results(0).asInstanceOf[CustomTransformer]

      result.params.length shouldBe 2

      result.params(0).name shouldBe "public param 1"
      result.params(0).parameterType shouldBe ParameterType.Choice

      result.params(1).name shouldBe "public param 2"
      result.params(1).parameterType shouldBe ParameterType.Choice
    }

    "create CustomTransformer without public params" in {
      val operation        = CreateCustomTransformer()
      val executionContext = createExecutionContext(createInnerWorkflow())

      val results = operation.executeUntyped(Vector.empty)(executionContext)
      results.size shouldBe 1
      results(0) shouldBe a[CustomTransformer]
      val result = results(0).asInstanceOf[CustomTransformer]

      result.params.length shouldBe 0
    }

    "infer parameters of CustomTransformer from input inner workflow" in {
      val operation = CreateCustomTransformer()
      val inferContext = createInferContext(
        createInnerWorkflow(
          PublicParam(node1Id, "target type", "public param 1"),
          PublicParam(node2Id, "target type", "public param 2")
        )
      )

      val results = operation.inferKnowledgeUntyped(Vector.empty)(inferContext)._1.map(_.single)
      results.length shouldBe 1
      results(0) shouldBe a[CustomTransformer]
      val result = results(0).asInstanceOf[CustomTransformer]

      result.params.length shouldBe 2

      result.params(0).name shouldBe "public param 1"
      result.params(0).parameterType shouldBe ParameterType.Choice

      result.params(1).name shouldBe "public param 2"
      result.params(1).parameterType shouldBe ParameterType.Choice
    }
  }

  private def createExecutionContext(innerWorkflow: InnerWorkflow): ExecutionContext = {
    val innerWorkflowExecutor = mock[InnerWorkflowExecutor]
    when(innerWorkflowExecutor.parse(any()))
      .thenReturn(innerWorkflow)

    val executionContext = mock[ExecutionContext]
    when(executionContext.innerWorkflowExecutor).thenReturn(innerWorkflowExecutor)
    executionContext
  }

  private def createInferContext(innerWorkflow: InnerWorkflow): InferContext = {
    val innerWorkflowExecutor = mock[InnerWorkflowExecutor]
    when(innerWorkflowExecutor.parse(any()))
      .thenReturn(innerWorkflow)

    val inferContext = mock[InferContext]
    when(inferContext.innerWorkflowParser).thenReturn(innerWorkflowExecutor)
    inferContext
  }

  private def createInnerWorkflow(publicParams: PublicParam*): InnerWorkflow = {
    val sourceNodeId = "2603a7b5-aaa9-40ad-9598-23f234ec5c32"
    val sinkNodeId   = "d7798d5e-b1c6-4027-873e-a6d653957418"

    val sourceNode = Node(sourceNodeId, Source())
    val sinkNode   = Node(sinkNodeId, Sink())

    val node1Operation = {
      val params = TypeConverter()
        .setTargetType(TargetTypeChoices.StringTargetTypeChoice())
        .setSelectedColumns(MultipleColumnSelection(Vector(NameColumnSelection(Set("column1")))))
        .paramValuesToJson
      new ConvertType().setParamsFromJson(params)
    }

    val node2Operation = {
      val params = TypeConverter()
        .setTargetType(TargetTypeChoices.StringTargetTypeChoice())
        .setSelectedColumns(MultipleColumnSelection(Vector(NameColumnSelection(Set("column1")))))
        .paramValuesToJson
      new ConvertType().setParamsFromJson(params)
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
