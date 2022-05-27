package ai.deepsense.deeplang.actions.exceptions

import ai.deepsense.deeplang.exceptions.DeepLangException

case class DeepSenseIOException(e: Throwable) extends DeepLangException(s"DeepSense IO Exception: ${e.getMessage}", e)
