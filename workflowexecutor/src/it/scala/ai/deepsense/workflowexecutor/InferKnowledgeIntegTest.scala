package ai.deepsense.workflowexecutor

import ai.deepsense.deeplang.catalogs.FlowCatalog
import ai.deepsense.deeplang.CatalogRecorder
import ai.deepsense.deeplang.Action
import ai.deepsense.deeplang.DeeplangIntegTestSupport
import ai.deepsense.deeplang.DeeplangTestSupport
import ai.deepsense.graph.DefaultKnowledgeService
import ai.deepsense.graph.Node
import ai.deepsense.graph.NodeInferenceImpl
import ai.deepsense.graph.NodeInferenceResult

class InferKnowledgeIntegTest extends DeeplangIntegTestSupport with DeeplangTestSupport {

  val nodeInference = new NodeInferenceImpl {}

  val FlowCatalog(_, doplCatalog, dopsCatalog) = CatalogRecorder.resourcesCatalogRecorder.catalogs

  val inferCtx                              = createInferContext(doplCatalog)

  for (operation <- dopsCatalog.operations.values) {
    operation.name should {
      "not throw in inferKnowledge" in {
        val op             = dopsCatalog.createDOperation(operation.id)
        val opNode         = Node[Action](operation.id, op)
        val inputKnowledge = DefaultKnowledgeService.defaultInputKnowledge(doplCatalog, op)
        noException should be thrownBy
          nodeInference.inferKnowledge(opNode, inferCtx, NodeInferenceResult(inputKnowledge))
      }
    }
  }

}
