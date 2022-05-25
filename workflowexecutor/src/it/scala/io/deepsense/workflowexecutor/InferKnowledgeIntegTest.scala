package io.deepsense.workflowexecutor

import io.deepsense.deeplang.catalogs.CatalogPair
import io.deepsense.deeplang.{CatalogRecorder, DOperation, DeeplangIntegTestSupport, DeeplangTestSupport}
import io.deepsense.graph.{DefaultKnowledgeService, Node, NodeInferenceImpl, NodeInferenceResult}

class InferKnowledgeIntegTest extends DeeplangIntegTestSupport with DeeplangTestSupport {
  val nodeInference = new NodeInferenceImpl{}

  val CatalogPair(doplCatalog, dopsCatalog) = CatalogRecorder.resourcesCatalogRecorder.catalogs
  val inferCtx = createInferContext(doplCatalog)

  for (operation <- dopsCatalog.operations.values) {
    operation.name should {
      "not throw in inferKnowledge" in {
        val op = dopsCatalog.createDOperation(operation.id)
        val opNode = Node[DOperation](operation.id, op)
        val inputKnowledge = DefaultKnowledgeService.defaultInputKnowledge(doplCatalog, op)
        noException should be thrownBy
          nodeInference.inferKnowledge(opNode, inferCtx, NodeInferenceResult(inputKnowledge))
      }
    }
  }
}
