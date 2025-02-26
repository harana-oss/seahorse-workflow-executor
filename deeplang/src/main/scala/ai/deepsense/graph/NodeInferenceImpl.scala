package ai.deepsense.graph

import scala.reflect.runtime.{universe => ru}

import ai.deepsense.deeplang.catalogs.actionobjects.ActionObjectCatalog
import ai.deepsense.deeplang.exceptions.FlowException
import ai.deepsense.deeplang.inference.InferContext
import ai.deepsense.deeplang.inference.InferenceWarnings
import ai.deepsense.deeplang.Knowledge
import ai.deepsense.deeplang.ActionObject
import ai.deepsense.deeplang.Action
import ai.deepsense.graph.FlowGraph.FlowNode
import ai.deepsense.graph.KnowledgeService._
import ai.deepsense.graph.TypesAccordance.TypesAccordance

trait NodeInferenceImpl extends NodeInference {

  /** @return inferred result for the given node. */
  override def inferKnowledge(
                               node: FlowNode,
                               context: InferContext,
                               inputInferenceForNode: NodeInferenceResult
  ): NodeInferenceResult = {

    val NodeInferenceResult(inKnowledge, warnings, errors) = inputInferenceForNode

    val parametersValidationErrors                         = node.value.validateParams

    def defaultInferenceResult(additionalErrors: Vector[FlowException] = Vector.empty) =
      createDefaultKnowledge(
        context.dOperableCatalog,
        node.value,
        warnings,
        (errors ++ parametersValidationErrors ++ additionalErrors).distinct
      )

    if (parametersValidationErrors.nonEmpty)
      defaultInferenceResult()
    else {
      try {
        val (outKnowledge, inferWarnings) =
          node.value.inferKnowledgeUntyped(inKnowledge)(context)
        NodeInferenceResult(outKnowledge, warnings ++ inferWarnings, errors)
      } catch {
        case exception: FlowException =>
          defaultInferenceResult(exception.toVector)
        case exception: Exception         =>
          defaultInferenceResult()
      }
    }
  }

  override def inputInferenceForNode(
                                      node: FlowNode,
                                      context: InferContext,
                                      graphKnowledge: GraphKnowledge,
                                      nodePredecessorsEndpoints: IndexedSeq[Option[Endpoint]]
  ): NodeInferenceResult = {

    (0 until node.value.inArity).foldLeft(NodeInferenceResult.empty) {
      case (NodeInferenceResult(knowledge, warnings, errors), portIndex) =>
        val predecessorEndpoint         = nodePredecessorsEndpoints(portIndex)
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
  }

  /** @return
    *   Input knowledge to be provided for the given input port and type accordance for edge incoming to this port.
    * @param node
    *   Node that contains input port.
    * @param catalog
    *   Catalog of registered ActionObjects.
    * @param graphKnowledge
    *   Contains inference results computed so far. This method assumes that graphKnowledge contains all required data.
    */
  private def inputKnowledgeAndAccordanceForInputPort(
                                                       node: FlowNode,
                                                       catalog: ActionObjectCatalog,
                                                       graphKnowledge: GraphKnowledge,
                                                       portIndex: Int,
                                                       predecessorEndpointOption: Option[Endpoint]
  ): (Knowledge[ActionObject], TypesAccordance) = {
    val inPortType = node.value.inPortTypes(portIndex).asInstanceOf[ru.TypeTag[ActionObject]]
    predecessorEndpointOption match {
      case None                      => (defaultKnowledge(catalog, inPortType), TypesAccordance.NotProvided(portIndex))
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
    *   Catalog of registered ActionObjects.
    */
  private def inputKnowledgeAndAccordanceForInputPort(
                                                       catalog: ActionObjectCatalog,
                                                       predecessorKnowledge: Knowledge[ActionObject],
                                                       portIndex: Int,
                                                       inPortType: ru.TypeTag[ActionObject]
  ): (Knowledge[ActionObject], TypesAccordance) = {
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
                                      catalog: ActionObjectCatalog,
                                      operation: Action,
                                      warnings: InferenceWarnings,
                                      errors: Vector[FlowException]
  ): NodeInferenceResult = {
    val outKnowledge = defaultOutputKnowledge(catalog, operation)
    NodeInferenceResult(outKnowledge, warnings, errors)
  }

}
