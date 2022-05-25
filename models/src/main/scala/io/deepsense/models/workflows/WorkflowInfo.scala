package io.deepsense.models.workflows

import org.joda.time.DateTime

case class WorkflowInfo(
  id: Workflow.Id,
  name: String,
  description: String,
  created: DateTime,
  updated: DateTime,
  ownerId: String,
  ownerName: String)

object WorkflowInfo {
  def empty(): WorkflowInfo =
    WorkflowInfo(Workflow.Id.randomId, "", "", DateTime.now, DateTime.now, "", "")

  def forId(id: Workflow.Id): WorkflowInfo =
    WorkflowInfo(id, "", "", DateTime.now, DateTime.now, "", "")
}
