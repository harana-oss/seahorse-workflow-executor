package io.deepsense.deeplang.inference.exceptions

import io.deepsense.deeplang.exceptions.DeepLangException

case class NoInputEdgesException(
    portIndex: Int)
  extends DeepLangException(s"Nothing is connected to the port $portIndex")
