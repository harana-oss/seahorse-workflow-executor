package io.deepsense.commons.rest.client.req

import io.deepsense.commons.models.Id

case class NotebookClientRequest(workflowId: Id, nodeId: Id, language: String)
