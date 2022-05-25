package io.deepsense.deeplang.utils

import spray.json.JsObject

import io.deepsense.deeplang.InnerWorkflowParser
import io.deepsense.deeplang.doperables.{CustomTransformer, ParamWithValues}
import io.deepsense.deeplang.params.custom.PublicParam

object CustomTransformerFactory {

  def createCustomTransformer(
      innerWorkflowParser: InnerWorkflowParser,
      innerWorkflowJson: JsObject): CustomTransformer = {
    val innerWorkflow = innerWorkflowParser.parse(innerWorkflowJson)
    val selectedParams: Seq[ParamWithValues[_]] =
        innerWorkflow.publicParams.flatMap {
      case PublicParam(nodeId, paramName, publicName) =>
        innerWorkflow.graph.nodes.find(_.id == nodeId)
          .flatMap(node => node.value.params.find(_.name == paramName)
          .map(p => {
            ParamWithValues(
              param = p.replicate(publicName),
              defaultValue = node.value.getDefault(p),
              setValue = node.value.get(p))
          }))
    }
    CustomTransformer(innerWorkflow, selectedParams)
  }
}
