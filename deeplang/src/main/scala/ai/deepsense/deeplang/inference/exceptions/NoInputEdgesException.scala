package ai.deepsense.deeplang.inference.exceptions

import ai.deepsense.deeplang.exceptions.FlowException

case class NoInputEdgesException(portIndex: Int)
    extends FlowException(s"Nothing is connected to the port $portIndex")
