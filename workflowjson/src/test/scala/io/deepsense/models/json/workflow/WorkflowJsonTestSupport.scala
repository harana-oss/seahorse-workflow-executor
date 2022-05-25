package io.deepsense.models.json.workflow

import scala.reflect.runtime.{universe => ru}

import io.deepsense.models.json.graph.GraphJsonProtocol.GraphReader

trait WorkflowJsonTestSupport
  extends WorkflowTestSupport
  with WorkflowJsonProtocol
  with HierarchyDescriptorJsonProtocol {

  override val graphReader: GraphReader = new GraphReader(catalog)
}
