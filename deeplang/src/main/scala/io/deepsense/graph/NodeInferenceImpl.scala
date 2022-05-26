package io.deepsense.graph

import scala.reflect.runtime.{universe => ru}

import io.deepsense.deeplang.catalogs.doperable.DOperableCatalog
import io.deepsense.deeplang.exceptions.DeepLangException
import io.deepsense.deeplang.inference.InferContext
import io.deepsense.deeplang.inference.InferenceWarnings
import io.deepsense.deeplang.DKnowledge
import io.deepsense.deeplang.DOperable
import io.deepsense.deeplang.DOperation
import io.deepsense.graph.DeeplangGraph.DeeplangNode
import io.deepsense.graph.DefaultKnowledgeService._
import io.deepsense.graph.TypesAccordance.TypesAccordance

trait NodeInferenceImpl extends NodeInference {

  /** @return inferred result for the given node. */
  override def inferKnowledge(
      node: DeeplangNode,
      context: InferContext,
      inputInferenceForNode: NodeInferenceResult
  ): NodeInferenceResult = {

    val NodeInferenceResult(inKnowledge, warnings, errors) = inputInferenceForNode
    val parametersValidationErrors                         = node.value.validateParams

    def defaultInferenceResult(additionalErrors: Vector[DeepLangException] = Vector.empty) =
      createDefaultKnowledge(
        context.dOperableCatalog,
        node.value,
        warnings,
        (errors ++ parametersValidationErrors ++ additionalErrors).distinct
      )

    if (parametersValidationErrors.nonEmpty)
      defaultInferenceResult()
    else
      try {
        val (outKnowledge, inferWarnings) =
          node.value.inferKnowledgeUntyped(inKnowledge)(context)
        NodeInferenceResult(outKnowledge, warnings ++ inferWarnings, errors)
      } catch {
        case exception: DeepLangException =>
          defaultInferenceResult(exception.toVector)
        case exception: Exception =>
          defaultInferenceResult()
      }
  }

  override def inputInferenceForNode(
      node: DeeplangNode,
      context: InferContext,
      graphKnowledge: GraphKnowledge,
      nodePredecessorsEndpoints: IndexedSeq[Option[Endpoint]]
  ): NodeInferenceResult =
    (0 until node.value.inArity).foldLeft(NodeInferenceResult.empty) {
      case (NodeInferenceResult(knowledge, warnings, errors), portIndex) =>
        val predecessorEndpoint = nodePredecessorsEndpoints(portIndex)
        val (portKnowledge, accordance) =
          inputKnowledgeAndAccordanceForInputPort(
            node,
            context.dOperableCatalog,
            graphKnowledge,
            portIndex,
            predecessorEndpoint
          )
        NodeInferenceResult(
          knowledge :+ portKnowledge,
          warnings ++ accordance.warnings,
          errors ++ accordance.errors
        )
    }

  /** @return
    *   Input knowledge to be provided for the given input port and type accordance for edge incoming to this port.
    * @param node
    *   Node that contains input port.
    * @param catalog
    *   Catalog of registered DOperables.
    * @param graphKnowledge
    *   Contains inference results computed so far. This method assumes that graphKnowledge contains all required data.
    */
  private def inputKnowledgeAndAccordanceForInputPort(
      node: DeeplangNode,
      catalog: DOperableCatalog,
      graphKnowledge: GraphKnowledge,
      portIndex: Int,
      predecessorEndpointOption: Option[Endpoint]
  ): (DKnowledge[DOperable], TypesAccordance) = {
    val inPortType = node.value.inPortTypes(portIndex).asInstanceOf[ru.TypeTag[DOperable]]
    predecessorEndpointOption match {
      case None => (defaultKnowledge(catalog, inPortType), TypesAccordance.NotProvided(portIndex))
      case Some(predecessorEndpoint) =>
        val outPortIndex         = predecessorEndpoint.portIndex
        val predecessorKnowledge = graphKnowledge.getKnowledge(predecessorEndpoint.nodeId)(outPortIndex)
        inputKnowledgeAndAccordanceForInputPort(catalog, predecessorKnowledge, portIndex, inPortType)
    }
  }

  /** @return
    *   Input knowledge to be provided for the given input port and type accordance for edge incoming to this port. If
    *   some (but not all) types matches input port type accordance, only matching types are placed in that port. If no
    *   types matches port, default knowledge is placed in this port.
    * @param predecessorKnowledge
    *   Inferred knowledge incoming to port.
    * @param portIndex
    *   Index of input port.
    * @param inPortType
    *   Type of input port.
    * @param catalog
    *   Catalog of registered DOperables.
    */
  private def inputKnowledgeAndAccordanceForInputPort(
      catalog: DOperableCatalog,
      predecessorKnowledge: DKnowledge[DOperable],
      portIndex: Int,
      inPortType: ru.TypeTag[DOperable]
  ): (DKnowledge[DOperable], TypesAccordance) = {
    val filteredTypes = predecessorKnowledge.filterTypes(inPortType.tpe)
    val filteredSize  = filteredTypes.size
    if (filteredSize == predecessorKnowledge.size)
      (filteredTypes, TypesAccordance.All())
    else if (filteredSize == 0)
      (defaultKnowledge(catalog, inPortType), TypesAccordance.None(portIndex))
    else
      (filteredTypes, TypesAccordance.Some(portIndex))
  }

  private def createDefaultKnowledge(
      catalog: DOperableCatalog,
      operation: DOperation,
      warnings: InferenceWarnings,
      errors: Vector[DeepLangException]
  ): NodeInferenceResult = {
    val outKnowledge = defaultOutputKnowledge(catalog, operation)
    NodeInferenceResult(outKnowledge, warnings, errors)
  }

}
