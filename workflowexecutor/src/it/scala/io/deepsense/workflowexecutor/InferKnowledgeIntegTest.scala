package io.deepsense.workflowexecutor

import io.deepsense.deeplang.catalogs.CatalogPair
import io.deepsense.deeplang.CatalogRecorder
import io.deepsense.deeplang.DOperation
import io.deepsense.deeplang.DeeplangIntegTestSupport
import io.deepsense.deeplang.DeeplangTestSupport
import io.deepsense.graph.DefaultKnowledgeService
import io.deepsense.graph.Node
import io.deepsense.graph.NodeInferenceImpl
import io.deepsense.graph.NodeInferenceResult

class InferKnowledgeIntegTest extends DeeplangIntegTestSupport with DeeplangTestSupport {

  val nodeInference = new NodeInferenceImpl {}

  val CatalogPair(doplCatalog, dopsCatalog) = CatalogRecorder.resourcesCatalogRecorder.catalogs

  val inferCtx = createInferContext(doplCatalog)

  for (operation <- dopsCatalog.operations.values)
    operation.name should {
      "not throw in inferKnowledge" in {
        val op             = dopsCatalog.createDOperation(operation.id)
        val opNode         = Node[DOperation](operation.id, op)
        val inputKnowledge = DefaultKnowledgeService.defaultInputKnowledge(doplCatalog, op)
        noException should be thrownBy
          nodeInference.inferKnowledge(opNode, inferCtx, NodeInferenceResult(inputKnowledge))
      }
    }

}
