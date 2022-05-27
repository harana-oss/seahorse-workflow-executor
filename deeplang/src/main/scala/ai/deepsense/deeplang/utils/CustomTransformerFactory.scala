package ai.deepsense.deeplang.utils

import ai.deepsense.deeplang.actionobjects.CustomTransformer
import ai.deepsense.deeplang.actionobjects.ParamWithValues
import ai.deepsense.deeplang.parameters.custom.InnerWorkflow
import ai.deepsense.deeplang.parameters.custom.PublicParam

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
