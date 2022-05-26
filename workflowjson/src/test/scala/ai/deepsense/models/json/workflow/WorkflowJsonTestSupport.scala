package ai.deepsense.models.json.workflow

import scala.reflect.runtime.{universe => ru}

import ai.deepsense.models.json.graph.GraphJsonProtocol.GraphReader

trait WorkflowJsonTestSupport
    extends WorkflowTestSupport
    with WorkflowJsonProtocol
    with HierarchyDescriptorJsonProtocol {

  override val graphReader: GraphReader = new GraphReader(catalog)

}
