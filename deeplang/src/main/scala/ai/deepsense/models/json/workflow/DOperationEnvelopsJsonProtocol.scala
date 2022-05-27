package ai.deepsense.models.json.workflow

import ai.deepsense.commons.json.envelope.EnvelopeJsonWriter
import ai.deepsense.deeplang.Action
import ai.deepsense.deeplang.catalogs.actions.ActionDescriptor

trait ActionsEnvelopeJsonProtocol extends ActionDescriptorJsonProtocol {

  val operationsEnvelopeLabel = "operations"

  implicit private val operationsFormat = ActionDescriptorBaseFormat

  implicit val operationsEnvelopeWriter =
    new EnvelopeJsonWriter[Map[Action.Id, ActionDescriptor]](operationsEnvelopeLabel)

}

trait ActionEnvelopeJsonProtocol extends ActionDescriptorJsonProtocol {

  implicit private val operationFormat = ActionDescriptorFullFormat

  val operationEnvelopeLabel = "operation"

  implicit val operationEnvelopeWriter =
    new EnvelopeJsonWriter[ActionDescriptor](operationEnvelopeLabel)

}

trait ActionEnvelopesJsonProtocol extends ActionsEnvelopeJsonProtocol with ActionEnvelopeJsonProtocol

object ActionEnvelopesJsonProtocol extends ActionEnvelopesJsonProtocol
