package ai.deepsense.deeplang.utils

import spray.json.JsObject

import ai.deepsense.deeplang.InnerWorkflowParser
import ai.deepsense.deeplang.doperables.CustomTransformer
import ai.deepsense.deeplang.doperables.ParamWithValues
import ai.deepsense.deeplang.params.custom.InnerWorkflow
import ai.deepsense.deeplang.params.custom.PublicParam

object CustomTransformerFactory {

  def createCustomTransformer(innerWorkflow: InnerWorkflow): CustomTransformer = {
    val selectedParams: Seq[ParamWithValues[_]] =
      innerWorkflow.publicParams.flatMap { case PublicParam(nodeId, paramName, publicName) =>
        innerWorkflow.graph.nodes
          .find(_.id == nodeId)
          .flatMap(node =>
            node.value.params
              .find(_.name == paramName)
              .map(p => {
                ParamWithValues(
                  param = p.replicate(publicName),
                  defaultValue = node.value.getDefault(p),
                  setValue = node.value.get(p)
                )
              })
          )
      }
    CustomTransformer(innerWorkflow, selectedParams)
  }

}
