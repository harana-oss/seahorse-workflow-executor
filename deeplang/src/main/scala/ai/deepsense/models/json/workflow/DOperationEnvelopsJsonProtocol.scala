package ai.deepsense.models.json.workflow

import ai.deepsense.commons.json.envelope.EnvelopeJsonWriter
import ai.deepsense.deeplang.Action
import ai.deepsense.deeplang.catalogs.actions.ActionDescriptor

trait ActionsEnvelopeJsonProtocol extends DOperationDescriptorJsonProtocol {

  val operationsEnvelopeLabel = "operations"

  implicit private val operationsFormat = DOperationDescriptorBaseFormat

  implicit val operationsEnvelopeWriter =
    new EnvelopeJsonWriter[Map[Action.Id, ActionDescriptor]](operationsEnvelopeLabel)

}

trait DOperationEnvelopeJsonProtocol extends DOperationDescriptorJsonProtocol {

  implicit private val operationFormat = DOperationDescriptorFullFormat

  val operationEnvelopeLabel = "operation"

  implicit val operationEnvelopeWriter =
    new EnvelopeJsonWriter[ActionDescriptor](operationEnvelopeLabel)

}

trait DOperationEnvelopesJsonProtocol extends ActionsEnvelopeJsonProtocol with DOperationEnvelopeJsonProtocol

object DOperationEnvelopesJsonProtocol extends DOperationEnvelopesJsonProtocol
