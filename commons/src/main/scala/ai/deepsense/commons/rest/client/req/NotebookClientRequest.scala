package ai.deepsense.commons.rest.client.req

import ai.deepsense.commons.models.Id

case class NotebookClientRequest(workflowId: Id, nodeId: Id, language: String)
