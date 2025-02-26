package ai.deepsense.deeplang.actions

import java.util.UUID

import scala.reflect.runtime.universe.TypeTag

import spray.json._

import ai.deepsense.commons.utils.Version
import ai.deepsense.deeplang.Action.Id
import ai.deepsense.deeplang._
import ai.deepsense.deeplang.documentation.OperationDocumentation
import ai.deepsense.deeplang.actionobjects.CustomTransformer
import ai.deepsense.deeplang.actions.custom.Sink
import ai.deepsense.deeplang.actions.custom.Source
import ai.deepsense.deeplang.inference.InferContext
import ai.deepsense.deeplang.inference.InferenceWarnings
import ai.deepsense.deeplang.parameters.custom.InnerWorkflow
import ai.deepsense.deeplang.parameters.Parameter
import ai.deepsense.deeplang.parameters.WorkflowParameter
import ai.deepsense.deeplang.utils.CustomTransformerFactory
import ai.deepsense.graph._
import ai.deepsense.models.json.graph.GraphJsonProtocol.GraphReader
import ai.deepsense.models.json.workflow.InnerWorkflowJsonReader

case class CreateCustomTransformer() extends TransformerAsFactory[CustomTransformer] with OperationDocumentation {

  override val id: Id = CreateCustomTransformer.id

  override val name: String = "Create Custom Transformer"

  override val description: String = "Creates custom transformer"

  override val since: Version = Version(1, 0, 0)

  val innerWorkflow = WorkflowParameter(name = "inner workflow", description = None)

  setDefault(innerWorkflow, CreateCustomTransformer.default)

  def getInnerWorkflow: InnerWorkflow = $(innerWorkflow)

  def setInnerWorkflow(workflow: JsObject, graphReader: GraphReader): this.type =
    set(innerWorkflow, InnerWorkflowJsonReader.toInner(workflow, graphReader))

  def setInnerWorkflow(workflow: InnerWorkflow): this.type = set(innerWorkflow, workflow)

  override val specificParams: Array[Parameter[_]] = Array(innerWorkflow)

  override lazy val tTagTO_0: TypeTag[CustomTransformer] = typeTag

  override def getDatasourcesIds: Set[UUID] = getInnerWorkflow.getDatasourcesIds

  override protected def execute()(context: ExecutionContext): CustomTransformer =
    CustomTransformerFactory.createCustomTransformer($(innerWorkflow))

  override def inferKnowledge()(context: InferContext): (Knowledge[CustomTransformer], InferenceWarnings) = {
    val transformer = CustomTransformerFactory.createCustomTransformer($(innerWorkflow))
    (Knowledge[CustomTransformer](transformer), InferenceWarnings.empty)
  }

  override def inferGraphKnowledgeForInnerWorkflow(context: InferContext): GraphKnowledge = {
    val innerWorkflowValue = getInnerWorkflow
    innerWorkflowValue.graph.inferKnowledge(context, GraphKnowledge())
  }

}

object CreateCustomTransformer {

  val id: Id = "65240399-2987-41bd-ba7e-2944d60a3404"

  private val sourceNodeId: Node.Id = "2603a7b5-aaa9-40ad-9598-23f234ec5c32"

  private val sinkNodeId: Node.Id = "d7798d5e-b1c6-4027-873e-a6d653957418"

  val default = InnerWorkflow(
    FlowGraph(
      nodes = Set(Node(sourceNodeId, Source()), Node(sinkNodeId, Sink())),
      edges = Set(Edge(Endpoint(sourceNodeId, 0), Endpoint(sinkNodeId, 0)))
    ),
    JsObject()
  )

}
