package io.deepsense.workflowexecutor.communication.message.notebook

import spray.json.DefaultJsonProtocol
import spray.json.RootJsonFormat

case class KernelManagerReady()

trait KernelManagerReadyJsonProtocol extends DefaultJsonProtocol {

  implicit val kernelManagerReadyFormat: RootJsonFormat[KernelManagerReady] =
    jsonFormat0(KernelManagerReady)

}

object KernelManagerReadyJsonProtocol extends KernelManagerReadyJsonProtocol
