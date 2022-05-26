package ai.deepsense.deeplang.inference.exceptions

import ai.deepsense.deeplang.exceptions.DeepLangException

case class NoInputEdgesException(portIndex: Int)
    extends DeepLangException(s"Nothing is connected to the port $portIndex")
