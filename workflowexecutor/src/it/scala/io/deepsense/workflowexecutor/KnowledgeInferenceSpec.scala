package io.deepsense.workflowexecutor

import io.deepsense.deeplang.catalogs.CatalogPair
import io.deepsense.deeplang.doperations.custom.Sink
import io.deepsense.deeplang.doperations.custom.Source
import io.deepsense.deeplang.doperations.CreateCustomTransformer
import io.deepsense.deeplang.doperations.DefaultCustomTransformerWorkflow
import io.deepsense.deeplang.inference.InferContext
import io.deepsense.deeplang.inference.exceptions.NoInputEdgesException
import io.deepsense.deeplang.params.custom.InnerWorkflow
import io.deepsense.deeplang.CatalogRecorder
import io.deepsense.deeplang.DOperation
import io.deepsense.deeplang.InnerWorkflowExecutor
import io.deepsense.deeplang.MockedInferContext
import io.deepsense.graph.AbstractInferenceSpec
import io.deepsense.graph.DeeplangGraph
import io.deepsense.graph.GraphKnowledge
import io.deepsense.graph.Node
import io.deepsense.models.json.graph.GraphJsonProtocol.GraphReader
import io.deepsense.models.json.workflow.InnerWorkflowJsonProtocol
import io.deepsense.workflowexecutor.executor.InnerWorkflowExecutorImpl

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
        createCustomTransformer.setInnerWorkflow(DefaultCustomTransformerWorkflow.defaultWorkflow)
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
          createCustomTransformer.setInnerWorkflow(innerWorkflow.toJson.asJsObject)
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

  implicit class DOperationTestExtension(val dOperation: DOperation) {

    def toNode(): Node[DOperation] = Node(Node.Id.randomId, dOperation)

  }

  private lazy val inferContext: InferContext = {
    val executor = new InnerWorkflowExecutorImpl(graphReader)
    MockedInferContext(dOperableCatalog = dOperableCatalog, innerWorkflowParser = executor)
  }

  override protected lazy val graphReader = new GraphReader(operationCatalog)

  private lazy val CatalogPair(dOperableCatalog, operationCatalog) =
    CatalogRecorder.resourcesCatalogRecorder.catalogs

}
