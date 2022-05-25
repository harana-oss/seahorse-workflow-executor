package io.deepsense.deeplang.doperations.exceptions

import io.deepsense.deeplang.exceptions.DeepLangException

case class DeepSenseUnknownHostException(e: Throwable)
  extends DeepLangException(s"Unknown host: ${e.getMessage}", e)
