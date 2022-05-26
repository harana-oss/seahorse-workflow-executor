package ai.deepsense.models.json.workflow

import ai.deepsense.commons.json.envelope.EnvelopeJsonWriter
import ai.deepsense.deeplang.DOperation
import ai.deepsense.deeplang.catalogs.doperations.DOperationDescriptor

trait DOperationsEnvelopeJsonProtocol extends DOperationDescriptorJsonProtocol {

  val operationsEnvelopeLabel = "operations"

  implicit private val operationsFormat = DOperationDescriptorBaseFormat

  implicit val operationsEnvelopeWriter =
    new EnvelopeJsonWriter[Map[DOperation.Id, DOperationDescriptor]](operationsEnvelopeLabel)

}

trait DOperationEnvelopeJsonProtocol extends DOperationDescriptorJsonProtocol {

  implicit private val operationFormat = DOperationDescriptorFullFormat

  val operationEnvelopeLabel = "operation"

  implicit val operationEnvelopeWriter =
    new EnvelopeJsonWriter[DOperationDescriptor](operationEnvelopeLabel)

}

trait DOperationEnvelopesJsonProtocol extends DOperationsEnvelopeJsonProtocol with DOperationEnvelopeJsonProtocol

object DOperationEnvelopesJsonProtocol extends DOperationEnvelopesJsonProtocol
