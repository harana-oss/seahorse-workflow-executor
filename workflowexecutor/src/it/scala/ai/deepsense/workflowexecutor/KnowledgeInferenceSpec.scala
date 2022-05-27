package ai.deepsense.workflowexecutor

import ai.deepsense.deeplang.catalogs.FlowCatalog
import ai.deepsense.deeplang.actions.custom.Sink
import ai.deepsense.deeplang.actions.custom.Source
import ai.deepsense.deeplang.actions.CreateCustomTransformer
import ai.deepsense.deeplang.inference.InferContext
import ai.deepsense.deeplang.inference.exceptions.NoInputEdgesException
import ai.deepsense.deeplang.parameters.custom.InnerWorkflow
import ai.deepsense.deeplang.CatalogRecorder
import ai.deepsense.deeplang.Action
import ai.deepsense.deeplang.MockedInferContext
import ai.deepsense.graph.AbstractInferenceSpec
import ai.deepsense.graph.DeeplangGraph
import ai.deepsense.graph.GraphKnowledge
import ai.deepsense.graph.Node
import ai.deepsense.models.json.graph.GraphJsonProtocol.GraphReader
import ai.deepsense.models.json.workflow.InnerWorkflowJsonProtocol

class KnowledgeInferenceSpec extends AbstractInferenceSpec with InnerWorkflowJsonProtocol {

  import spray.json._

  "Graph" should {
    "infer knowledge for nested nodes (ex. CreateCustomTransformer)" in {
      val rootWorkflowWithSomeInnerWorkflow = {
        val createCustomTransformer = operationCatalog
          .createDOperation(
            CreateCustomTransformer.id
          )
          .asInstanceOf[CreateCustomTransformer]

        createCustomTransformer.setInnerWorkflow(CreateCustomTransformer.default)
        DeeplangGraph(Set(createCustomTransformer.toNode()))
      }

      val inferenceResult = rootWorkflowWithSomeInnerWorkflow.inferKnowledge(
        inferContext,
        GraphKnowledge()
      )

      // There is 1 node in root workflow and two more in inner workflow.
      inferenceResult.results.size should be > 1
    }
  }

  "Node errors" should {
    "be properly inferred for inner workflow. For example" when {
      "sink node has no input connected " in {
        val sinkExpectedToHaveErrors = operationCatalog.createDOperation(Sink.id).toNode()
        val rootWorkflowWithInvalidInnerWorkflow = {
          val createCustomTransformer = operationCatalog
            .createDOperation(
              CreateCustomTransformer.id
            )
            .asInstanceOf[CreateCustomTransformer]

          val innerWorkflow = {
            val source = operationCatalog.createDOperation(Source.id).toNode()
            val graph  = DeeplangGraph(Set(source, sinkExpectedToHaveErrors), Set.empty)
            InnerWorkflow(graph, JsObject(), List.empty)
          }
          createCustomTransformer.setInnerWorkflow(innerWorkflow)
          DeeplangGraph(Set(createCustomTransformer.toNode()), Set.empty)
        }

        val inferenceResult = rootWorkflowWithInvalidInnerWorkflow.inferKnowledge(
          inferContext,
          GraphKnowledge()
        )

        inferenceResult.errors(sinkExpectedToHaveErrors.id).head should matchPattern { case NoInputEdgesException(0) =>
        }
      }
    }
  }

  implicit class DOperationTestExtension(val dOperation: Action) {

    def toNode(): Node[Action] = Node(Node.Id.randomId, dOperation)

  }

  private lazy val inferContext: InferContext =
    MockedInferContext(dOperableCatalog = dOperableCatalog)

  override protected lazy val graphReader = new GraphReader(operationCatalog)

  private lazy val FlowCatalog(_, dOperableCatalog, operationCatalog) =
    CatalogRecorder.resourcesCatalogRecorder.catalogs

}
