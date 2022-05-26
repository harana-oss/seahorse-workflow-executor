package ai.deepsense.deeplang.doperations.exceptions

import ai.deepsense.deeplang.exceptions.DeepLangException

case class DeepSenseUnknownHostException(e: Throwable) extends DeepLangException(s"Unknown host: ${e.getMessage}", e)
